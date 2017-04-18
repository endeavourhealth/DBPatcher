package org.endeavourhealth.dbpatcher.exceptions;

public class DBPatcherException extends Exception {
    static final long serialVersionUID = 1L;

    public DBPatcherException() {
        super();
    }
    public DBPatcherException(String message) {
        super(message);
    }
    public DBPatcherException(String message, Throwable cause) {
        super(message, cause);
    }
    public DBPatcherException(Throwable cause) {
        super(cause);
    }
}
