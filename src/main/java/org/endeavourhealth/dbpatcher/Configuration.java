package org.endeavourhealth.dbpatcher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.endeavourhealth.dbpatcher.databaseXml.Connection;
import org.endeavourhealth.dbpatcher.databaseXml.Database;
import org.endeavourhealth.dbpatcher.exceptions.DBPatcherException;
import org.endeavourhealth.dbpatcher.helpers.LogHelper;
import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;
import org.endeavourhealth.dbpatcher.helpers.XmlHelper;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.function.Function;

public class Configuration {
    private final static String DATABASE_SCHEMA_FILENAME = "database.xsd";

    private final static LogHelper LOG = LogHelper.getLogger(Configuration.class);

    private String hostname;
    private int port;
    private String databaseName;
    private String username;
    private String password;
    private String basePath;
    private String schemaPath;
    private String functionsPath;
    private String triggersPath;
    private String scriptsPath;
    private boolean dropFunctions;
    private boolean autoDropFunctions;

    public Configuration(Arguments arguments) throws DBPatcherException {
        determineConfiguration(arguments);
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getSchemaPath() {
        return schemaPath;
    }

    public String getFunctionsPath() {
        return functionsPath;
    }

    public String getTriggersPath() {
        return triggersPath;
    }

    public String getScriptsPath() { return scriptsPath; }

    public boolean getDropFunctions() {
        return (dropFunctions || autoDropFunctions);
    }

    public boolean getAutoDropFunctions() {
        return autoDropFunctions;
    }

    private void determineConfiguration(Arguments arguments) throws DBPatcherException {

        File xmlFile = getDatabaseXmlFile(arguments.getDatabaseXmlPath());
        Database database = getDatabaseXml(xmlFile);

        LOG.info("Using configuration:");

        printConfiguration("Database xml file", getCanonicalPath(xmlFile));

        Connection connection = database.getConnection();

        this.hostname = getOptionalValue("Hostname", whenNotNull(connection, t -> t.getHostname()), arguments.getHostOverride());
        String port = getOptionalValue("Port", whenNotNull(connection, t -> Integer.toString(t.getPort())), arguments.getPortOverride());
        this.databaseName = getOptionalValue("Database name", whenNotNull(connection, t -> t.getDatabaseName()), arguments.getDbNameOverride());
        this.username = getOptionalValue("Username", whenNotNull(connection, t -> t.getUsername()), arguments.getUsernameOverride());
        this.password = getOptionalValue("Password", whenNotNull(connection, t -> t.getPassword()), arguments.getPasswordOverride(), true);

        if (StringUtils.isEmpty(this.hostname))
            throw new DBPatcherException("Empty hostname");

        if (StringUtils.isEmpty(port))
            throw new DBPatcherException("Empty port");

        this.port = Integer.parseInt(port);

        if (StringUtils.isEmpty(port))
            throw new DBPatcherException("Empty database name");

        if (StringUtils.isEmpty(this.username))
            throw new DBPatcherException("Empty username");

        this.basePath = xmlFile.getParentFile().getPath();
        printPath("base", this.basePath);

        this.schemaPath = getAndPrintCanonicalPath("schema", database.getPaths().getSchema());
        this.functionsPath = getAndPrintCanonicalPath("functions", database.getPaths().getFunctions());
        this.triggersPath = getAndPrintCanonicalPath("triggers", database.getPaths().getTriggers());
        this.scriptsPath = getAndPrintCanonicalPath("scripts", database.getPaths().getScripts());

        this.dropFunctions = arguments.getDropFunctions();
        this.autoDropFunctions = arguments.getAutoDropFunctions();
    }

    private <T, R> R whenNotNull(T obj, Function<T, R> func) {
        if (obj != null)
            return func.apply(obj);

        return null;
    }

    private String getOptionalValue(String valueName, String value, String overriddenValue) {
        return getOptionalValue(valueName, value, overriddenValue, false);
    }

    private String getOptionalValue(String valueName, String value, String overriddenValue, boolean hidePrintedValue) {
        if (overriddenValue != null) {
            printConfiguration(valueName + " (overridden)", overriddenValue, hidePrintedValue);
            return overriddenValue;
        }

        printConfiguration(valueName, value, hidePrintedValue);
        return value;
    }

    private void printConfiguration(String name, String value) {
        printConfiguration(name, value, false);
    }

    private void printConfiguration(String name, String value, boolean hideValue) {
        String printedValue = value;

        if (hideValue)
            printedValue = "(value hidden)";

        LOG.info(" " + StringUtils.rightPad(name + ":  ", 27) + printedValue);
    }

    private String getAndPrintCanonicalPath(String pathName, String relativePathValue) throws DBPatcherException {
        if (relativePathValue == null)
            return null;

        if (StringUtils.isEmpty(relativePathValue))
            throw new DBPatcherException(WordUtils.capitalize(pathName) + " path is empty");

        File path = new File(Paths.get(this.basePath, relativePathValue).toString());

        if (!path.isDirectory() || (!path.exists()))
            throw new DBPatcherException("Could not find " + pathName + " path '" + getCanonicalPath(path) + "'");

        printPath(pathName, getCanonicalPath(path));

        return getCanonicalPath(path);
    }

    private void printPath(String pathName, String pathValue) throws DBPatcherException {
        printConfiguration(WordUtils.capitalize(pathName) + " path", getCanonicalPath(new File(pathValue)));
    }

    private static String getCanonicalPath(File file) throws DBPatcherException {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new DBPatcherException("Error attempting to resolve path '" + file.getAbsolutePath());
        }
    }

    private static Database getDatabaseXml(File xmlFile) throws DBPatcherException {
        try {
            String xsd = getDatabaseXsd();
            String xml = FileUtils.readFileToString(xmlFile, StandardCharsets.UTF_8);

            XmlHelper.validate(xml, xsd);

            return XmlHelper.deserialize(xml, Database.class);
        } catch (DBPatcherException e) {
            throw e;
        } catch (Exception e) {
            throw new DBPatcherException("Error reading '" + xmlFile.getPath() + "' - " + e.getMessage(), e);
        }
    }

    private static String getDatabaseXsd() throws DBPatcherException {
        try {
            return ResourceHelper.getResourceAsString(DATABASE_SCHEMA_FILENAME);
        } catch (Exception e) {
            throw new DBPatcherException("Error reading database XSD resource - " + e.getMessage(), e);
        }
    }

    private static File getDatabaseXmlFile(String path) throws DBPatcherException {
        File xmlFile = new File(path);

        if (!xmlFile.isFile() || (!xmlFile.exists()))
            throw new DBPatcherException("Could not find database configuration file '" + path + "'");

        return xmlFile;
    }
}
