package org.endeavourhealth.dbpatcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.flywaydb.core.Flyway;
import org.slf4j.LoggerFactory;

public class DBPatcher {

    private ConfigParser configParser;

    public DBPatcher(String databaseXmlPath, String jdbcUrlOverride, String usernameOverride, String passwordOverride) throws DBPatcherException {
        this.configParser = new ConfigParser(databaseXmlPath, jdbcUrlOverride, usernameOverride, passwordOverride);
    }

    public void patch() {
        System.out.println(Main.DIVIDER);
        System.out.println("Calling flyway to commence patching...");

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);

        Flyway flyway = new Flyway();
        flyway.setDataSource(configParser.getJdbcUrl(), configParser.getUsername(), configParser.getPassword());
        flyway.setLocations("filesystem:" + configParser.getSchemaPath());
        flyway.setSqlMigrationSeparator("-");

        FlywayCallback flywayCallback = new FlywayCallback();
        flyway.setCallbacks(flywayCallback);

        flyway.migrate();
    }

}
