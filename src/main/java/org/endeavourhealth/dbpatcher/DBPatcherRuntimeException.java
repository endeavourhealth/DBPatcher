package org.endeavourhealth.dbpatcher;

public class DBPatcherRuntimeException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public DBPatcherRuntimeException() {
        super();
    }

    public DBPatcherRuntimeException(String message) {
        super(message);
    }

    public DBPatcherRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBPatcherRuntimeException(Throwable cause) {
        super(cause);
    }
}
