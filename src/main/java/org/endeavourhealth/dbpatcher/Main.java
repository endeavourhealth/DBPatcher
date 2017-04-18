package org.endeavourhealth.dbpatcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.helpers.LogHelper;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private static final LogHelper LOG = LogHelper.getLogger(Main.class);

    private final static String ARG_HELP = "--help";
    private final static String ARG_URL = "--url";
    private final static String ARG_USERNAME = "--username";
    private final static String ARG_PASSWORD = "--password";

    public static void main(String[] args) {
        try {
            configureLogback();

            LOG.infoWithDivider("DBPatcher v1.0");

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

            LOG.infoWithDivider("PATCH SUCCESS");

        } catch (Exception e) {
            LOG.error("", e);
            LOG.errorWithDivider("PATCH FAILURE");
        }
    }

    private static void printHelp() {
        LOG.info("");
        LOG.info(" usage:  java -jar DBPatcher.jar database.xml [options]");
        LOG.info("");
        LOG.info(" options:  " + ARG_URL + "=jdbc-url    Overrides jdbc url in database.xml");
        LOG.info("           " + ARG_USERNAME + "=user   Overrides username in database.xml");
        LOG.info("           " + ARG_PASSWORD + "=pass   Overrides password in database.xml");
        LOG.info("");
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

    private static void configureLogback() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger logger = context.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.INFO);

        ConsoleAppender<ILoggingEvent> appender = (ConsoleAppender<ILoggingEvent>) logger.getAppender("console");

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("[%-4level] %message%n");
        encoder.start();
        appender.setEncoder(encoder);
    }
}
