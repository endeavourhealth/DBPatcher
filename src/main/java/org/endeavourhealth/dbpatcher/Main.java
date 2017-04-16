package org.endeavourhealth.dbpatcher;

import org.flywaydb.core.Flyway;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        System.out.println("Endeavour DBPatcher");

        File file = new File("sql");

        if (!file.exists() || !file.isDirectory()) {
            System.err.println("Could not find sql directory at " + file.getAbsolutePath());
        }

        Flyway flyway = new Flyway();

    }
}
