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
        LOG.info("");
        LOG.info(" options:  " + Arguments.ARG_HOST + "=hostname    Overrides Connection/Hostname in database.xml");
        LOG.info("           " + Arguments.ARG_PORT + "=port        Overrides Connection/Port in database.xml");
        LOG.info("           " + Arguments.ARG_DBNAME + "=dbname        Overrides Connection/DatabaseName in database.xml");
        LOG.info("           " + Arguments.ARG_USERNAME + "=username    Overrides Connection/Username in database.xml");
        LOG.info("           " + Arguments.ARG_PASSWORD + "=password    Overrides Connection/Password in database.xml");
        LOG.info("");
        LOG.info("           " + Arguments.ARG_HELP + "             Print this message");
        LOG.info("");
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
