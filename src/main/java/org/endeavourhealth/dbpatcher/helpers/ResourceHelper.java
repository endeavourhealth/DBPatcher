package org.endeavourhealth.dbpatcher.helpers;

import org.apache.commons.io.IOUtils;
import org.endeavourhealth.dbpatcher.Main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class ResourceHelper {
    public static String getResourceAsString(String url) throws IOException, URISyntaxException {
        return IOUtils.toString(Main.class.getResource("/" + url).toURI(), StandardCharsets.UTF_8);
    }
}
