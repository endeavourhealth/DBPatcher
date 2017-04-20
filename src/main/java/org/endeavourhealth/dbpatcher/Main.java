package org.endeavourhealth.dbpatcher;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.exceptions.DBPatcherException;
import org.endeavourhealth.dbpatcher.helpers.Argument;
import org.endeavourhealth.dbpatcher.helpers.FileHelper;
import org.endeavourhealth.dbpatcher.helpers.LogHelper;
import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
            String targetFilePath = Argument.getFirstArg(args);
            ensureNotOptionalArg(targetFilePath);
            String[] optionalArgs = Argument.removeFirstArg(args);

            String zipFileDirectory = null;

            try {
                Configuration configuration = null;

                if (looksLikeXmlFile(targetFilePath)) {
                    configuration = Configuration.processConfiguration(targetFilePath, optionalArgs);

                } else if (looksLikeZipFile(targetFilePath)) {
                    zipFileDirectory = getZipFileDirectory(targetFilePath);
                    unzip(targetFilePath, zipFileDirectory);

                    configuration = Configuration.processConfiguration(targetFilePath, zipFileDirectory, optionalArgs);

                } else {
                    throw new DBPatcherException("File extension is not .xml or .zip");
                }

                DBPatcher dbPatcher = new DBPatcher(configuration);
                dbPatcher.patch();

            } finally {
                removeZipFileDirectory(zipFileDirectory);
            }

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
        LOG.info("           " + padHelpArg(Arguments.ARG_XML + "=DB_XML_FILE") + "Specify DB_XML_FILE file name in root of");
        LOG.info("           " + padHelpArg("") + "ZIP file; default database.xml");
        LOG.info("");
        LOG.info("           " + padHelpArg(Arguments.ARG_DROPFNS) + "Prompts to drop all functions before");
        LOG.info("           " + padHelpArg("") + "applying fns in DB_XML_FILE");
        LOG.info("           " + padHelpArg(Arguments.ARG_AUTODROPFNS) + "Automatically drops all functions before");
        LOG.info("           " + padHelpArg("") + "applying fns in DB_XML_FILE");
        LOG.info("");
        LOG.info("           " + padHelpArg(Arguments.ARG_SAMPLE_XML) + "Write sample DB_XML_FILE file");
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
        String filename = ResourceFileNames.SAMPLE_DATABASE_XML;
        LOG.info("Generating " + filename);

        String sampleDatabaseXml = ResourceHelper.getResourceAsString(filename);
        FileHelper.writeUtf8ToNewFile(filename, sampleDatabaseXml);

        LOG.info("Completed");
    }

    private static void ensureNotOptionalArg(String targetFile) throws DBPatcherException {
        if (targetFile.startsWith("-"))
            throw new DBPatcherException("Expecting DB_XML_FILE or ZIP_FILE in place of '" + targetFile + "'");
    }

    private static boolean looksLikeZipFile(String targetFile) {
        return (targetFile.toLowerCase().endsWith(".zip"));
    }

    private static boolean looksLikeXmlFile(String targetFile) {
        return (targetFile.toLowerCase().endsWith(".xml"));
    }

    private static String getZipFileDirectory(String zipFilePath) throws DBPatcherException {
        String extractLocation = zipFilePath + "-extracted";

        File extractDir = new File(zipFilePath);
        Integer count = 1;

        while (extractDir.exists()) {
            extractDir = new File(extractLocation + count.toString());
            count++;

            if (count > 100)
                throw new DBPatcherException("Cannot find location to extract ZIP to");
        }

        return extractDir.getPath();
    }

    private static void unzip(String file, String directory) throws ZipException {
        LOG.info("Unzipping '" + file + "'");
        ZipFile zipFile = new ZipFile(file);
        zipFile.extractAll(directory);
    }

    private static void removeZipFileDirectory(String directory) throws IOException {
        if (directory != null)
            FileUtils.deleteDirectory(new File(directory));
    }
}
