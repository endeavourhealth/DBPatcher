package org.endeavourhealth.dbpatcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.helpers.Argument;
import org.endeavourhealth.dbpatcher.helpers.FileHelper;
import org.endeavourhealth.dbpatcher.helpers.LogHelper;
import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;
import org.slf4j.LoggerFactory;

public class Main {

    private static final LogHelper LOG = LogHelper.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            configureLogback();

            LOG.infoWithDivider("DBPatcher v1.0");

            if (ArrayUtils.isEmpty(args) || (Argument.hasArg(args, Arguments.ARG_HELP))) {
                printHelp();
                return;
            }

            if (Argument.hasArg(args, Arguments.ARG_SAMPLE_XML)) {
                generateSampleXml();
                return;
            }

        } catch (Exception e) {
            LOG.error(e);
            return;
        }

        try {
            Arguments arguments = new Arguments(args);

            DBPatcher dbPatcher = new DBPatcher(arguments);
            dbPatcher.patch();

            LOG.infoWithDivider("PATCH SUCCESS");

        } catch (Exception e) {
            LOG.error(e);
            LOG.errorWithDivider("PATCH FAILURE");
        }
    }

    private static void printHelp() {
        LOG.info("");
        LOG.info(" usage:  java -jar DBPatcher.jar DB_XML_FILE [options]");
        LOG.info("    or:  java -jar DBPatcher.jar ZIP_FILE [options]");
        LOG.info("");
        LOG.info(" options:  " + padHelpArg(Arguments.ARG_HOST + "=hostname") + "Override hostname in DB_XML_FILE");
        LOG.info("           " + padHelpArg(Arguments.ARG_PORT + "=port") + "Override port in DB_XML_FILE");
        LOG.info("           " + padHelpArg(Arguments.ARG_DBNAME + "=dbname") + "Override databasename in DB_XML_FILE");
        LOG.info("           " + padHelpArg(Arguments.ARG_USERNAME + "=username") + "Override username in DB_XML_FILE");
        LOG.info("           " + padHelpArg(Arguments.ARG_PASSWORD + "=password") + "Override password in DB_XML_FILE");
        LOG.info("");
        LOG.info("           " + padHelpArg(Arguments.ARG_XML + "=DB_XML_FILE") + "Specify DB_XML_FILE file name in root of ZIP file; default database.xml");
        LOG.info("");
        LOG.info("           " + padHelpArg(Arguments.ARG_DROPFNS) + "Prompts to drop all functions before applying fns in DB_XML_FILE");
        LOG.info("           " + padHelpArg(Arguments.ARG_AUTODROPFNS) + "Automatically drops all functions before applying fns in DB_XML_FILE");
        LOG.info("");
        LOG.info("           " + padHelpArg(Arguments.ARG_SAMPLE_XML) + "Write sample DB_XML_FILE file in current dir");
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

    private static void generateSampleXml() throws Exception {
        final String name = "sample-database.xml";

        LOG.info("Generating " + name);

        String sampleDatabaseXml = ResourceHelper.getResourceAsString(name);
        FileHelper.writeUtf8ToNewFile(name, sampleDatabaseXml);

        LOG.info("Completed");
    }
}
