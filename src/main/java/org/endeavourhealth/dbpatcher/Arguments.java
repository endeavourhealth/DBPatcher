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

    private String targetFilePath;
    private String hostOverride;
    private String portOverride;
    private String dbNameOverride;
    private String usernameOverride;
    private String passwordOverride;
    private boolean dropFunctions = false;
    private boolean autoDropFunctions = false;

    public Arguments(String[] args) throws DBPatcherException {

        List<Argument> arguments = Argument.parseArgs(args);

        if (arguments.size() == 0)
            throw new DBPatcherException("No arguments found");

        Argument firstArgument = arguments.remove(0);

        if (firstArgument.getArg().startsWith("-"))
            throw new DBPatcherException("Expecting DB_XML_FILE or ZIP_FILE in place of '" + firstArgument.getArg() + "'");

        this.targetFilePath = firstArgument.getArg();

        for (Argument argument : arguments) {

            switch (argument.getKey()) {
                case ARG_HOST: hostOverride = argument.getRequiredArgValue(); break;
                case ARG_PORT: portOverride = argument.getRequiredArgValue(); break;
                case ARG_DBNAME: dbNameOverride = argument.getRequiredArgValue(); break;
                case ARG_USERNAME: usernameOverride = argument.getRequiredArgValue(); break;
                case ARG_PASSWORD: passwordOverride = argument.getRequiredArgValue(); break;
                case ARG_DROPFNS: argument.ensureNoValue(); dropFunctions = true; break;
                case ARG_AUTODROPFNS: argument.ensureNoValue(); autoDropFunctions = true; break;
                default: throw new DBPatcherException("Option '" + argument.getArg() + "' not recognised");
            }
        }
    }

    public String getTargetFilePath() {
        return targetFilePath;
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
}
