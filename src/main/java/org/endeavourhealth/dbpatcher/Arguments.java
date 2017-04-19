package org.endeavourhealth.dbpatcher;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.exceptions.DBPatcherException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Arguments {
    public final static String ARG_HELP = "--help";
    public final static String ARG_SAMPLE_XML = "--generatesamplexml";
    public final static String ARG_HOST = "--host";
    public final static String ARG_PORT = "--port";
    public final static String ARG_DBNAME = "--db";
    public final static String ARG_USERNAME = "--user";
    public final static String ARG_PASSWORD = "--pass";
    public final static String ARG_DROPFNS = "--dropfns";
    public final static String ARG_AUTODROPFNS = "--autodropfns";

    private String databaseXmlPath;
    private String hostOverride;
    private String portOverride;
    private String dbNameOverride;
    private String usernameOverride;
    private String passwordOverride;
    private boolean dropFunctions = false;
    private boolean autoDropFunctions = false;

    public Arguments(String[] args) throws DBPatcherException {
        databaseXmlPath = getFirstArg(args);

        HashMap<String, String> argumentMap = parseOptionalArgs(skipFirstArg(args));

        for (String argKey : argumentMap.keySet()) {
            if (argKey.equals(ARG_HOST))
                hostOverride = argumentMap.get(ARG_HOST);
            else if (argKey.equals(ARG_PORT))
                portOverride = argumentMap.get(ARG_PORT);
            else if (argKey.equals(ARG_DBNAME))
                dbNameOverride = argumentMap.get(ARG_DBNAME);
            else if (argKey.equals(ARG_USERNAME))
                usernameOverride = argumentMap.get(ARG_USERNAME);
            else if (argKey.equals(ARG_PASSWORD))
                passwordOverride = argumentMap.get(ARG_PASSWORD);
            else if (argKey.equals(ARG_DROPFNS))
                dropFunctions = true;
            else if (argKey.equals(ARG_AUTODROPFNS))
                autoDropFunctions = true;
            else
                throw new DBPatcherException("Option '" + argKey + "' not recognised");
        }
    }

    public static boolean hasArg(String[] args, String arg) {
        return Arrays
                .stream(args)
                .anyMatch(t -> t.equals(arg));
    }

    public String getDatabaseXmlPath() {
        return databaseXmlPath;
    }

    public String getHostOverride() {
        return hostOverride;
    }

    public String getPortOverride() {
        return portOverride;
    }

    public String getDbNameOverride() {
        return dbNameOverride;
    }

    public String getUsernameOverride() {
        return usernameOverride;
    }

    public String getPasswordOverride() {
        return passwordOverride;
    }

    public Boolean getDropFunctions() {
        return dropFunctions;
    }

    public Boolean getAutoDropFunctions() {
        return autoDropFunctions;
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
}
