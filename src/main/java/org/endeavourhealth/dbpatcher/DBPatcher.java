package org.endeavourhealth.dbpatcher;

import org.flywaydb.core.Flyway;

public class DBPatcher {

    private ConfigParser configParser;

    public DBPatcher(String databaseXmlPath, String jdbcUrlOverride, String usernameOverride, String passwordOverride) throws DBPatcherException {
        this.configParser = new ConfigParser(databaseXmlPath, jdbcUrlOverride, usernameOverride, passwordOverride);
    }

    public void patch() {
        System.out.println(Main.DIVIDER);
        System.out.println("Commence patching...");

        Flyway flyway = new Flyway();
        flyway.setLocations(configParser.getSchemaPath());
        flyway.migrate();
    }

}
