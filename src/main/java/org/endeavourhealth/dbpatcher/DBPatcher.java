package org.endeavourhealth.dbpatcher;

import org.endeavourhealth.dbpatcher.configuration.Database;

public class DBPatcher {

    private Database database;
    private String url;
    private String username;
    private String password;

    public DBPatcher(Database database, String url, String username, String password) {
        this.database = database;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void patch() {

    }
}
