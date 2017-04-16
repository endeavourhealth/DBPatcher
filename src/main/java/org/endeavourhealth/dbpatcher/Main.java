package org.endeavourhealth.dbpatcher;

import org.apache.commons.io.Charsets;

import javax.annotation.Resources;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws URISyntaxException, IOException {

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
        String xml = null;

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

            if (xml != null) {
                System.out.println("Unrecognised argument '" + arg + "'");
                return;
            }

            xml = arg;
        }

        File xmlFile = new File(xml);

        if (!xmlFile.isFile() || (!xmlFile.exists())) {
            System.out.println("Could not find database configuration file '" + xml + "'");
            return;
        }


    }
}
