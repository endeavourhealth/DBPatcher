package org.endeavourhealth.dbpatcher.helpers;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.exceptions.DBPatcherException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
    Parses a list of arguments in the format
        arg1 arg2=value arg3 arg4 arg5=value ...
 */
public class Argument {

    public static List<Argument> parseArgs(String[] args) throws DBPatcherException {
        List<Argument> result = new ArrayList<>();

        for (String arg : args) {
            String[] parts = arg.split("=", -1);

            if ((parts.length == 0) || (parts.length > 2))
                throw new DBPatcherException("Could not parse option '" + arg);

            String key = StringUtils.trim(parts[0]);
            String value = null;

            if (parts.length > 1)
                value = StringUtils.trim(parts[1]);

            if (result.stream().anyMatch(t -> t.key.equals(key)))
                throw new DBPatcherException("Duplicate option '" + key + "' found");

            Argument argument = new Argument(arg, key, value);

            result.add(argument);
        }

        return result;
    }

    public static boolean hasArg(String[] args, String arg) {
        return Arrays
                .stream(args)
                .anyMatch(t -> t.equals(arg));
    }

    public static String getFirstArg(String[] args) {
        if (args != null)
            if (args.length > 0)
                return args[0];

        return null;
    }

    public static String[] removeFirstArg(String[] args) {
        if (args == null)
            return null;

        return Arrays
                .stream(args)
                .skip(1)
                .collect(Collectors.toList())
                .toArray(new String[0]);
    }

    private String arg;
    private String key;
    private String value;

    public Argument(String arg, String key, String value) {
        this.arg = arg;
        this.key = key;
        this.value = value;
    }

    public String getArg() {
        return arg;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getRequiredArgValue() throws DBPatcherException {
        if (StringUtils.isEmpty(value))
            throw new DBPatcherException(key + " specified but no value found");

        return value;
    }

    public void ensureNoValue() throws DBPatcherException {
        if (StringUtils.isNotEmpty(value))
            throw new DBPatcherException(key + " does not require a value");
    }
}
