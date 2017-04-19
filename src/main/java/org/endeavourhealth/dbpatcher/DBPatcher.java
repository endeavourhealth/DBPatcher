package org.endeavourhealth.dbpatcher;

import org.endeavourhealth.dbpatcher.dataLayer.DataLayer;
import org.endeavourhealth.dbpatcher.dataLayer.MasterDataLayer;
import org.endeavourhealth.dbpatcher.exceptions.DBPatcherException;
import org.endeavourhealth.dbpatcher.exceptions.DBPatcherRuntimeException;
import org.endeavourhealth.dbpatcher.helpers.ConsoleHelper;
import org.endeavourhealth.dbpatcher.helpers.LogHelper;
import org.endeavourhealth.dbpatcher.helpers.FileHelper;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBPatcher extends FlywayCallback {

    private static LogHelper LOG = LogHelper.getLogger(DBPatcher.class);
    private static final String POSTGRES_MASTER_DB = "postgres";

    private Configuration configuration;
    private DataLayer dataLayer;

    public DBPatcher(Arguments arguments) throws DBPatcherException {
        this.configuration = new Configuration(arguments);
    }

    public void patch() throws IOException, SQLException, DBPatcherException {
        checkIfDatabaseExists();
        startFlyway();
    }

    private void checkIfDatabaseExists() throws IOException, SQLException, DBPatcherException {
        DataSource masterDataSource = getDataSource(POSTGRES_MASTER_DB);
        MasterDataLayer masterDataLayer = new MasterDataLayer(masterDataSource);

        LOG.infoWithDivider("Looking for database");

        if (!masterDataLayer.doesDatabaseExist(this.configuration.getDatabaseName())) {
            LOG.info("> Could not find database '" + this.configuration.getDatabaseName() + "', create database?");

            if (!ConsoleHelper.readYesNoFromConsole())
                throw new DBPatcherException("Database '" + this.configuration.getDatabaseName() + "' does not exist");

            LOG.info("Creating database '" + this.configuration.getDatabaseName() + "'");
            masterDataLayer.createDatabase(this.configuration.getDatabaseName());
        } else {
            LOG.info("Database '" + this.configuration.getDatabaseName() + "' found");
        }
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

    private void startFlyway() {
        LOG.infoWithDivider("Calling flyway to commence patching...");

        DataSource dataSource = getDataSource(configuration.getDatabaseName());

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("filesystem:" + configuration.getSchemaPath());
        flyway.setSqlMigrationSeparator("-");
        flyway.setCallbacks(this);
        flyway.migrate();
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
                dropUserFunctions();
                applyUserFunctions();
            }

        } catch (Exception e) {
            throw new DBPatcherRuntimeException(e.getMessage(), e);
        }
    }

    private void dropUserFunctions() throws IOException, SQLException {

        List<String> functionsToDrop = dataLayer.getUserFunctionSignatures();

        if (functionsToDrop.size() == 0)
            return;

        LOG.info("Drop " + Integer.toString(functionsToDrop.size()) + " existing user function(s) on DB first?");
        LOG.info("");

        int count = 1;

        for (String line : functionsToDrop)
            LOG.info(" " + Integer.toString(count++) + ". " + line);

        LOG.info("");
        LOG.info("> Drop " + Integer.toString(functionsToDrop.size()) + " existing user function(s) on DB first?");

        if (ConsoleHelper.readYesNoFromConsole()) {
            LOG.info("");
            LOG.info("Dropping existing user function(s)");
            dataLayer.dropUserFunctions(functionsToDrop);
        }

        LOG.info("");
    }

    private void applyUserFunctions() throws SQLException, IOException {
        List<File> functionsToApply = FileHelper.findSqlFilesRecursive(configuration.getFunctionsPath());

        if (functionsToApply.size() == 0)
            return;

        LOG.info("Applying " + Integer.toString(functionsToApply.size()) + " user function(s)...");

        int count = 1;
        for (File file : functionsToApply) {
            LOG.info(" " + Integer.toString(count++) + ". " + file.getName());

            String sql = FileHelper.readUtf8ToString(file);
            dataLayer.executeSql(sql);
        }
    }
}
