package org.endeavourhealth.dbpatcher;

import org.endeavourhealth.dbpatcher.dataLayer.DataLayer;
import org.endeavourhealth.dbpatcher.dataLayer.MasterDataLayer;
import org.endeavourhealth.dbpatcher.dataLayer.model.Function;
import org.endeavourhealth.dbpatcher.exceptions.DBPatcherException;
import org.endeavourhealth.dbpatcher.exceptions.DBPatcherRuntimeException;
import org.endeavourhealth.dbpatcher.helpers.ConsoleHelper;
import org.endeavourhealth.dbpatcher.helpers.LogHelper;
import org.endeavourhealth.dbpatcher.helpers.FileHelper;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

public class DBPatcher extends FlywayCallback {

    private static LogHelper LOG = LogHelper.getLogger(DBPatcher.class);
    private static final String POSTGRES_MASTER_DB = "postgres";

    private Configuration configuration;
    private DataLayer dataLayer;

    public DBPatcher(Arguments arguments) throws DBPatcherException {
        this.configuration = new Configuration(arguments);
    }

    public void patch() throws Exception {
        checkIfDatabaseExists();
        startFlyway();
    }

    private void checkIfDatabaseExists() throws Exception {
        MasterDataLayer masterDataLayer = new MasterDataLayer(getDataSource(POSTGRES_MASTER_DB));

        LOG.infoWithDivider("Looking for database");

        if (!masterDataLayer.doesDatabaseExist(this.configuration.getDatabaseName())) {
            LOG.info("> Could not find database '" + this.configuration.getDatabaseName() + "', create database?");

            if (!ConsoleHelper.readYesNoFromConsole())
                throw new DBPatcherException("Database '" + this.configuration.getDatabaseName() + "' does not exist");

            LOG.info("> Are you sure you wish to create a new database with name '" + this.configuration.getDatabaseName() + "' on host '" + this.configuration.getHostname() + "'?");

            if (!ConsoleHelper.readConfirmationFromConsole("Please type the database name to confirm", this.configuration.getDatabaseName()))
                throw new DBPatcherException("Database '" + this.configuration.getDatabaseName() + "' does not exist");

            LOG.info("Creating database '" + this.configuration.getDatabaseName() + "'");
            masterDataLayer.createDatabase(this.configuration.getDatabaseName());
        } else {
            LOG.info("Database '" + this.configuration.getDatabaseName() + "' found");
        }
    }

    private void startFlyway() {
        LOG.infoWithDivider("Calling flyway to commence patching");

        Flyway flyway = new Flyway();
        flyway.setDataSource(getDataSource(configuration.getDatabaseName()));
        flyway.setLocations("filesystem:" + configuration.getSchemaPath());
        flyway.setSqlMigrationSeparator("-");
        flyway.setCallbacks(this);
        flyway.migrate();
    }

    private PGSimpleDataSource getDataSource(String databaseName) {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setServerName(configuration.getHostname());
        pgSimpleDataSource.setPortNumber(configuration.getPort());
        pgSimpleDataSource.setDatabaseName(databaseName);
        pgSimpleDataSource.setUser(configuration.getUsername());
        pgSimpleDataSource.setPassword(configuration.getPassword());
        return pgSimpleDataSource;
    }

    @Override
    public void beforeMigrate(Connection connection) {
        this.dataLayer = new DataLayer(connection);

        LOG.infoWithDivider("Migrating schema");
    }

    @Override
    public void afterMigrate(Connection connection) {
        try {
            if (configuration.getFunctionsPath() != null) {
                LOG.infoWithDivider("Applying functions");

                if (configuration.getDropFunctions())
                    dropUserFunctions();

                applySqlItems(configuration.getFunctionsPath(), "user function(s)");
            }

            if (configuration.getTriggersPath() != null) {
                LOG.infoWithDivider("Applying triggers");

                applySqlItems(configuration.getTriggersPath(), "trigger(s)");
            }

            if (configuration.getScriptsPath() != null) {
                LOG.infoWithDivider("Applying scripts");

                applySqlItems(configuration.getScriptsPath(), "script(s)");
            }

        } catch (Exception e) {
            throw new DBPatcherRuntimeException(e.getMessage(), e);
        }
    }

    private void applySqlItems(String path, String itemType) throws Exception {
        List<File> sqlItemsToApply = FileHelper.findSqlFilesRecursive(path);

        if (sqlItemsToApply.size() == 0)
            return;

        LOG.info("Applying " + Integer.toString(sqlItemsToApply.size()) + " " + itemType + ":");
        LOG.info("");

        int count = 1;
        for (File file : sqlItemsToApply) {
            LOG.printNumberedListItem(file.getName(), count++);

            String sql = FileHelper.readUtf8ToString(file);
            dataLayer.executeSql(sql);
        }
    }

    private void dropUserFunctions() throws Exception {

        List<Function> functionsToDrop = dataLayer.getUserFunctions();
        List<String> functionSignaturesToDrop = functionsToDrop
                .stream()
                .map(t -> t.getFunctionSignature())
                .collect(Collectors.toList());

        if (functionsToDrop.size() == 0)
            return;

        boolean dropFunctions = false;

        if (configuration.getAutoDropFunctions()) {
            dropFunctions = true;
        } else {
            String question = "Drop " + Integer.toString(functionsToDrop.size()) + " existing user function(s) on DB first?";

            LOG.info(question);
            LOG.info("");

            LOG.printNumberedList(functionSignaturesToDrop);

            LOG.info("");
            LOG.info("> " + question);

            dropFunctions = ConsoleHelper.readYesNoFromConsole();

            LOG.info("");
        }

        if (dropFunctions) {
            LOG.info("Dropping " + Integer.toString(functionsToDrop.size()) + " existing user function(s):");
            LOG.info("");
            LOG.printNumberedList(functionSignaturesToDrop);

            dataLayer.dropUserFunctions(functionsToDrop);
        }

        LOG.info("");
    }
}
