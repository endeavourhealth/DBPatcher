package org.endeavourhealth.dbpatcher.helpers;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ResourceHelper {
    public static URL getResourceAsURLObject(String url) throws IOException {
        return com.google.common.io.Resources.getResource(url);
    }
    public static String getResourceAsString(String url) throws IOException {
        URL urlItem = getResourceAsURLObject(url);
        return com.google.common.io.Resources.toString(urlItem, StandardCharsets.UTF_8);
    }
}
