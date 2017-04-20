package org.endeavourhealth.dbpatcher.dataLayer;

import org.endeavourhealth.dbpatcher.ResourceFileNames;
import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;

public class MasterDataLayer {

    private DataSource dataSource;

    public MasterDataLayer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createDatabase(String databaseName) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                String sql = ResourceHelper.getResourceAsString(ResourceFileNames.CREATE_DATABASE_SQL);

                sql = MessageFormat.format(sql, databaseName);

                statement.execute(sql);
            }
        }
    }

    public boolean doesDatabaseExist(String databaseName) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                String sql = ResourceHelper.getResourceAsString(ResourceFileNames.DOES_DATABASE_EXIST_SQL);

                sql = MessageFormat.format(sql, databaseName);

                try (ResultSet resultSet = statement.executeQuery(sql)) {

                    resultSet.next();
                    return resultSet.getBoolean("database_exists");
                }
            }
        }
    }
}
