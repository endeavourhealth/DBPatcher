package org.endeavourhealth.dbpatcher;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private final static String ARG_HELP = "--help";
    private final static String ARG_URL = "--url";
    private final static String ARG_USERNAME = "--username";
    private final static String ARG_PASSWORD = "--password";
    final static String DIVIDER = "------------------------------------------------------------";

    public static void main(String[] args) throws Exception {
        try {
            System.out.println(DIVIDER);
            System.out.println("DBPatcher v1.0");
            System.out.println(DIVIDER);

            if (ArrayUtils.isEmpty(args) || (hasHelpArg(args))) {
                printHelp();
                return;
            }

            String databaseXmlPath = getFirstArg(args);

            HashMap<String, String> arguments = parseOptionalArgs(skipFirstArg(args));

            String urlOverride = null;
            String usernameOverride = null;
            String passwordOverride = null;

            for (String argKey : arguments.keySet()) {
                if (argKey.equals(ARG_URL))
                    urlOverride = arguments.get(ARG_URL);
                else if (argKey.equals(ARG_USERNAME))
                    usernameOverride = arguments.get(ARG_USERNAME);
                else if (argKey.equals(ARG_PASSWORD))
                    passwordOverride = arguments.get(ARG_PASSWORD);
                else
                    throw new DBPatcherException("Option '" + argKey + "' not recognised");
            }

            DBPatcher dbPatcher = new DBPatcher(databaseXmlPath, urlOverride, usernameOverride, passwordOverride);
            dbPatcher.patch();

        } catch (DBPatcherException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
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

    private static String getFirstArg(String args[]) {
        if (args != null)
            if (args.length > 0)
                return args[0];

        return null;
    }

    private static List<String> skipFirstArg(String[] args) {
        return Arrays.stream(args)
                .skip(1).collect(Collectors.toList());
    }

    private static HashMap<String, String> parseOptionalArgs(List<String> args) throws DBPatcherException {
        HashMap<String, String> result = new HashMap<>();

        for (String arg : args) {
            String[] parts = arg.split("=", -1);

            if ((parts.length == 0) || (parts.length > 2))
                throw new DBPatcherException("Could not parse option '" + arg);

            String key = StringUtils.trim(parts[0]);
            String value = null;

            if (parts.length > 1)
                value = StringUtils.trim(parts[1]);

            if (result.containsKey(key))
                throw new DBPatcherException("Duplicate option '" + key + "' found");

            result.put(key, value);
        }

        return result;
    }

    private static boolean hasHelpArg(String[] args) {
        return Arrays
                .stream(args)
                .anyMatch(t -> t.equals(ARG_HELP));
    }
}
