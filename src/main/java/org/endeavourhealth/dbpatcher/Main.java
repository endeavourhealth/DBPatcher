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

public class Main {

    private static final LogHelper LOG = LogHelper.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            configureLogback();

            LOG.infoWithDivider("DBPatcher v1.0");

            if (ArrayUtils.isEmpty(args) || (Arguments.hasHelpArg(args))) {
                printHelp();
                return;
            }

            Arguments arguments = new Arguments(args);

            DBPatcher dbPatcher = new DBPatcher(arguments);
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
        LOG.info(" usage:  java -jar DBPatcher.jar database.zip [options]");
        LOG.info("");
        LOG.info(" options:  " + padHelpArg(Arguments.ARG_HOST + "=hostname") + "Overrides Connection/Hostname");
        LOG.info("           " + padHelpArg(Arguments.ARG_PORT + "=port") + "Overrides Connection/Port");
        LOG.info("           " + padHelpArg(Arguments.ARG_DBNAME + "=dbname") + "Overrides Connection/DatabaseName");
        LOG.info("           " + padHelpArg(Arguments.ARG_USERNAME + "=username") + "Overrides Connection/Username");
        LOG.info("           " + padHelpArg(Arguments.ARG_PASSWORD + "=password") + "Overrides Connection/Password");
        LOG.info("");
        LOG.info("           " + padHelpArg(Arguments.ARG_DROPFNS) + "Prompts to drop all user functions before applying functions in Paths/Functions");
        LOG.info("           " + padHelpArg(Arguments.ARG_AUTODROPFNS) + "Automatically drops all user functions before applying functions in Paths/Functions");
        LOG.info("");
        LOG.info("           " + padHelpArg(Arguments.ARG_HELP) + "Print this message");
        LOG.info("");
    }

    private static String padHelpArg(String arg) {
        return StringUtils.rightPad(arg, 20);
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
