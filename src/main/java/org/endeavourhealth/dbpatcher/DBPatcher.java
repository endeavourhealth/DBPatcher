package org.endeavourhealth.dbpatcher;

import org.endeavourhealth.dbpatcher.helpers.ConsoleHelper;
import org.endeavourhealth.dbpatcher.helpers.LogHelper;
import org.endeavourhealth.dbpatcher.helpers.FileHelper;
import org.flywaydb.core.Flyway;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DBPatcher extends FlywayCallback {

    private static LogHelper LOG = LogHelper.getLogger(DBPatcher.class);

    private ConfigParser configParser;
    private DataLayer dataLayer;

    public DBPatcher(String databaseXmlPath, String jdbcUrlOverride, String usernameOverride, String passwordOverride) throws DBPatcherException {
        this.configParser = new ConfigParser(databaseXmlPath, jdbcUrlOverride, usernameOverride, passwordOverride);
    }

    public void patch() {
        startFlyway();
    }

    private void startFlyway() {
        LOG.infoWithDivider("Calling flyway to commence patching...");

        Flyway flyway = new Flyway();
        flyway.setDataSource(configParser.getJdbcUrl(), configParser.getUsername(), configParser.getPassword());
        flyway.setLocations("filesystem:" + configParser.getSchemaPath());
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
            if (configParser.getFunctionsPath() != null) {
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
        List<File> functionsToApply = FileHelper.findSqlFilesRecursive(configParser.getFunctionsPath());

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
