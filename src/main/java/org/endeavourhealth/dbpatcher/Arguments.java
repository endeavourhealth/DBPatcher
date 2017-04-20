package org.endeavourhealth.dbpatcher;

import org.endeavourhealth.dbpatcher.exceptions.DBPatcherException;
import org.endeavourhealth.dbpatcher.helpers.Argument;

import java.util.List;

public class Arguments {

    public final static String ARG_HELP = "--help";
    public final static String ARG_SAMPLE_XML = "--generatesamplexml";
    public final static String ARG_HOST = "--host";
    public final static String ARG_PORT = "--port";
    public final static String ARG_DBNAME = "--db";
    public final static String ARG_USERNAME = "--user";
    public final static String ARG_PASSWORD = "--pass";
    public final static String ARG_XML = "--xml";
    public final static String ARG_DROPFNS = "--dropfns";
    public final static String ARG_AUTODROPFNS = "--autodropfns";

    private String hostOverride;
    private String portOverride;
    private String dbNameOverride;
    private String usernameOverride;
    private String passwordOverride;
    public String xmlFileInZipPath;
    private boolean dropFunctions = false;
    private boolean autoDropFunctions = false;

    public Arguments(String[] args) throws DBPatcherException {

        List<Argument> arguments = Argument.parseArgs(args);

        for (Argument argument : arguments) {

            switch (argument.getKey()) {
                case ARG_HOST: hostOverride = argument.getRequiredArgValue(); break;
                case ARG_PORT: portOverride = argument.getRequiredArgValue(); break;
                case ARG_DBNAME: dbNameOverride = argument.getRequiredArgValue(); break;
                case ARG_USERNAME: usernameOverride = argument.getRequiredArgValue(); break;
                case ARG_PASSWORD: passwordOverride = argument.getRequiredArgValue(); break;
                case ARG_XML: xmlFileInZipPath = argument.getRequiredArgValue(); break;
                case ARG_DROPFNS: argument.ensureNoValue(); dropFunctions = true; break;
                case ARG_AUTODROPFNS: argument.ensureNoValue(); autoDropFunctions = true; break;
                default: throw new DBPatcherException("Option '" + argument.getArg() + "' not recognised");
            }
        }
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
    public String getXmlFileInZipPath() { return xmlFileInZipPath; }
}
