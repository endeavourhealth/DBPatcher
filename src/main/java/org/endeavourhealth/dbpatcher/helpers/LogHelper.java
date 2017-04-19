package org.endeavourhealth.dbpatcher.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class LogHelper {

    private final static String DIVIDER = "------------------------------------------------------------";

    private Logger logger;

    public LogHelper(Logger logger) {
        this.logger = logger;
    }

    public static LogHelper getLogger(Class<?> currentClass) {
        return new LogHelper(LoggerFactory.getLogger(currentClass));
    }

    public void info(String line) {
        logger.info(line);
    }

    public void info(List<String> lines) {
        info(lines, "");
    }

    public void error(String line) {
        logger.error(line);
    }

    public void error(String line, Object var2) {
        logger.error(line, var2);
    }

    public void error(Exception e) {
        logger.error("", e);
    }

    public void info(List<String> lines, String linePrefix) {

    }

    public void infoDivider() {
        info(DIVIDER);
    }

    public void errorWithDivider(String line) {
        error(DIVIDER);
        error(line);
        error(DIVIDER);
    }

    public void infoWithDivider(String line) {
        info(DIVIDER);
        info(line);
        info(DIVIDER);
    }
}
