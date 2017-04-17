package org.endeavourhealth.dbpatcher;

import org.apache.commons.io.Charsets;

import java.io.IOException;
import java.net.URL;

public class ResourceHelper {
    public static URL getResourceAsURLObject(String url) throws IOException {
        URL urlItem = com.google.common.io.Resources.getResource(url);
        return urlItem;
    }
    public static String getResourceAsString(String url) throws IOException {
        URL urlItem = getResourceAsURLObject(url);
        String text = com.google.common.io.Resources.toString(urlItem, Charsets.UTF_8);
        return text;
    }
}
