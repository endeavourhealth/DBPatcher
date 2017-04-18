package org.endeavourhealth.dbpatcher.helpers;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileHelper {
    public static String readUtf8ToString(String path) throws IOException {
        return FileUtils.readFileToString(new File(path), "UTF-8");
    }

    public static String readUtf8ToString(File file) throws IOException {
        return FileUtils.readFileToString(file, "UTF-8");
    }

    public static List<File> findSqlFilesRecursive(String path) {
        return new ArrayList<>(FileUtils.listFiles(new File(path), new String[] { "sql" }, true));
    }
}
