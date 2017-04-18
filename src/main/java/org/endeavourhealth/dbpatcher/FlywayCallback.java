package org.endeavourhealth.dbpatcher;

import com.mysql.cj.api.mysqla.result.Resultset;
import jdk.nashorn.internal.objects.annotations.Function;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;
import org.flywaydb.core.api.MigrationInfo;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

public class FlywayCallback implements org.flywaydb.core.api.callback.FlywayCallback {

    private Collection<File> functions;
    private Collection<File> triggers;

    public FlywayCallback(Collection<File> functions, Collection<File> triggers) {
        this.functions = functions;
        this.triggers = triggers;
    }

    @Override
    public void beforeClean(Connection connection) {

    }

    @Override
    public void afterClean(Connection connection) {

    }

    @Override
    public void beforeMigrate(Connection connection) {
        try {

            System.out.println(Main.DIVIDER);
            System.out.println("Pre-migration steps...");
            System.out.println(Main.DIVIDER);

            DataLayer dataLayer = new DataLayer(connection);
            List<String> userFunctions = null;

                System.out.println("Current user functions:");
                 userFunctions = dataLayer.getUserFunctionSignatures();

                for (String userFunction : userFunctions)
                    System.out.println(" " + userFunction);

            while (true) {
                System.out.print("Remove above user functions? ([Y]es, [n]o) ");

                String line = System.console().readLine().trim();

                if (line.equals("Y")) {
                    dataLayer.dropUserFunctions(userFunctions);
                    break;
                } else if (line.equals("n")) {
                    break;
                }
            }


            System.out.println(Main.DIVIDER);
            System.out.println("Migrating schema...");
            System.out.println(Main.DIVIDER);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void afterMigrate(Connection connection) {
        try {
            System.out.println(Main.DIVIDER);
            System.out.println("Post-migration steps");
            System.out.println(Main.DIVIDER);

            if (functions != null) {
                if (functions.size() != 0) {
                    System.out.println("Applying user function(s)...");

                    for (File file : functions) {

                        System.out.println(" " + file.getName());

                        String sql = FileUtils.readFileToString(file, "UTF-8");

                        try (Statement statement = connection.createStatement()) {
                            statement.execute(sql);
                        }
                    }
                }
            }

            System.out.println(Main.DIVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeEachMigrate(Connection connection, MigrationInfo migrationInfo) {

    }

    @Override
    public void afterEachMigrate(Connection connection, MigrationInfo migrationInfo) {

    }

    @Override
    public void beforeValidate(Connection connection) {

    }

    @Override
    public void afterValidate(Connection connection) {

    }

    @Override
    public void beforeBaseline(Connection connection) {

    }

    @Override
    public void afterBaseline(Connection connection) {

    }

    @Override
    public void beforeRepair(Connection connection) {

    }

    @Override
    public void afterRepair(Connection connection) {

    }

    @Override
    public void beforeInfo(Connection connection) {

    }

    @Override
    public void afterInfo(Connection connection) {

    }
}
