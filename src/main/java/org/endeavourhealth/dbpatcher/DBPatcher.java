package org.endeavourhealth.dbpatcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.apache.commons.io.FileUtils;
import org.flywaydb.core.Flyway;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;

public class DBPatcher {

    private ConfigParser configParser;

    public DBPatcher(String databaseXmlPath, String jdbcUrlOverride, String usernameOverride, String passwordOverride) throws DBPatcherException {
        this.configParser = new ConfigParser(databaseXmlPath, jdbcUrlOverride, usernameOverride, passwordOverride);
    }

    public void patch() {
        System.out.println(Main.DIVIDER);
        System.out.println("Calling flyway to commence patching...");
        System.out.println(Main.DIVIDER);

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);

        Flyway flyway = new Flyway();
        flyway.setDataSource(configParser.getJdbcUrl(), configParser.getUsername(), configParser.getPassword());
        flyway.setLocations("filesystem:" + configParser.getSchemaPath());
        flyway.setSqlMigrationSeparator("-");

        FlywayCallback flywayCallback = new FlywayCallback(getFunctions(), getTriggers());
        flyway.setCallbacks(flywayCallback);

        flyway.migrate();


    }

    private Collection<File> getFunctions() {
        return FileUtils.listFiles(new File(configParser.getFunctionsPath()), new String[] { "sql"}, true);
    }

    private Collection<File> getTriggers() {
        return FileUtils.listFiles(new File(configParser.getTriggersPath()), new String[] { ".sql"}, true);
    }
}
