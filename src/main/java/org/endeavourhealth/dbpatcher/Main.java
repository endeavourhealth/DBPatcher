package org.endeavourhealth.dbpatcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.configuration.Database;
import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;
import org.endeavourhealth.dbpatcher.helpers.XmlHelper;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private final static String ARG_HELP = "--help";
    private final static String ARG_URL = "--url";
    private final static String ARG_USERNAME = "--username";
    private final static String ARG_PASSWORD = "--password";
    private final static String DATABASE_SCHEMA_FILENAME = "database.xsd";

    public static void main(String[] args) throws Exception {

        System.out.println("DBPatcher v1.0");

        if (ArrayUtils.isEmpty(args) || (hasArgument(args, ARG_HELP))) {
            printHelp();
            return;
        }

        String databaseXmlPath = getFirstArgument(args);
        String url = getArgumentValue(args, ARG_URL);
        String username = getArgumentValue(args, ARG_USERNAME);
        String password = getArgumentValue(args, ARG_PASSWORD);

        Database database = getDatabaseXml(databaseXmlPath);

        DBPatcher dbPatcher = new DBPatcher(database, url, username, password);
        dbPatcher.patch();
    }

    private static void printHelp() {
        System.out.println("");
        System.out.println(" usage:  java -jar DBPatcher.jar database.xml [options]");
        System.out.println("");
        System.out.println(" options:  " + ARG_URL + "=jdbc-url    Overrides jdbc url in database.xml");
        System.out.println("           " + ARG_USERNAME + "=user   Overrides username in database.xml");
        System.out.println("           " + ARG_PASSWORD + "=pass   Overrides password in database.xml");
        System.out.println("");
    }

    private static String getFirstArgument(String args[]) {
        if (args != null)
            if (args.length > 0)
                return args[0];

        return null;
    }

    private static String getArgumentValue(String[] args, String argKey) {
        String arg = getArgument(args, argKey);

        if (arg == null)
            return null;

        String[] argParts = StringUtils.split(arg, "=");

        if (argParts.length != 2)
            throw new IllegalArgumentException("Could not parse argument '" + arg + "'");

        return StringUtils.defaultString(argParts[1]).trim();
    }

    private static String getArgument(String[] args, String argKey) {
        List<String> matchingArgs = Arrays.stream(args)
                .filter(t -> StringUtils.defaultString(t).trim().toLowerCase().startsWith(argKey))
                .collect(Collectors.toList());

        if (matchingArgs.size() == 0)
            return null;

        if (matchingArgs.size() > 1)
            throw new IllegalArgumentException("'" + argKey + "' specified more than once");

        return matchingArgs.get(0);
    }

    private static boolean hasArgument(String[] args, String argKey) {
        return (getArgument(args, argKey) != null);
    }

    private static Database getDatabaseXml(String databaseXmlPath) throws IOException, XMLStreamException {
        File xmlFile = new File(databaseXmlPath);

        if (!xmlFile.isFile() || (!xmlFile.exists())) {
            System.out.println("Could not find database configuration file '" + databaseXmlPath + "'");
            return null;
        }

        String xsd = ResourceHelper.getResourceAsString(DATABASE_SCHEMA_FILENAME);
        String xml = FileUtils.readFileToString(xmlFile, StandardCharsets.UTF_8);

        try {
            XmlHelper.validate(xml, xsd);
        } catch (Exception e) {
            System.out.println("Error reading '" + databaseXmlPath + "' - " + e.getMessage());
            return null;
        }

        return XmlHelper.deserialize(xml, Database.class);
    }
}
