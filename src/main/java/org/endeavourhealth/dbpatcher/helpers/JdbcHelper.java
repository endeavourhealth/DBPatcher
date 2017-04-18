package org.endeavourhealth.dbpatcher.helpers;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class JdbcHelper {

    public static String getDatabaseName(String connectString) throws MalformedURLException {
        String cleanedString = null;
        int schemeEndOffset = connectString.indexOf("://");

        if (schemeEndOffset == -1)
            cleanedString = "http://" + connectString;
        else
            cleanedString = "http" + connectString.substring(schemeEndOffset);

        URL connectUrl = new URL(cleanedString);
        String databaseName = connectUrl.getPath();

        if (null == databaseName)
            return null;

        while (databaseName.startsWith("/"))
            databaseName = databaseName.substring(1);

        return databaseName;
    }
}
