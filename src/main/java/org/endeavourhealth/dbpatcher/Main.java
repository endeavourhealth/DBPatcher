package org.endeavourhealth.dbpatcher;

import org.apache.commons.io.FileUtils;
import org.flywaydb.core.Flyway;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        final String urlArg = "--url";
        final String usernameArg = "--username";
        final String passwordArg = "--password";

        System.out.println("DBPatcher v1.0");

        if ((args == null) || (args.length == 0)) {
            System.out.println("");
            System.out.println(" usage:  java -jar DBPatcher.jar database.xml");
            System.out.println("");
            System.out.println(" options:  " + urlArg + "=jdbc-url    Overrides jdbc url in database.xml");
            System.out.println("           " + usernameArg + "=user   Overrides username in database.xml");
            System.out.println("           " + passwordArg + "=pass   Overrides password in database.xml");
            System.out.println("");

            return;
        }

        String url = null;
        String username = null;
        String password = null;
        String xmlPath = null;

        for (String arg : args) {
            if (arg.startsWith("-")) {
                if (arg.startsWith(urlArg + "="))
                    url = arg.substring(urlArg.length() + 1).trim();
                else if (arg.startsWith(usernameArg + "="))
                    username = arg.trim().substring(usernameArg.length() + 1).trim();
                else if (arg.startsWith(passwordArg + "="))
                    password = arg.trim().substring(passwordArg.length() + 1).trim();

                System.out.println("Unrecognised option '" + arg + "'");
                return;
            }

            if (xmlPath != null) {
                System.out.println("Unrecognised argument '" + arg + "'");
                return;
            }

            xmlPath = arg;
        }

        File xmlFile = new File(xmlPath);

        if (!xmlFile.isFile() || (!xmlFile.exists())) {
            System.out.println("Could not find database configuration file '" + xmlPath + "'");
            return;
        }

        String xsd = ResourceHelper.getResourceAsString("database.xsd");
        String xml = FileUtils.readFileToString(xmlFile, "UTF-8");

        try {
            XmlHelper.validate(xml, xsd);
        } catch (Exception e) {
            System.out.println("Error reading '" + xmlPath + "' - " + e.getMessage());
        }

        Flyway flyway = new Flyway();

    }
}
