package org.endeavourhealth.dbpatcher.dataLayer;

import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

public class MasterDataLayer {

    private DataSource dataSource;

    public MasterDataLayer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createDatabase(String databaseName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("create database " + databaseName + ";");
            }
        }
    }

    public boolean doesDatabaseExist(String databaseName) throws IOException, SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {

                String sql = ResourceHelper.getResourceAsString("postgresql/does-database-exist.sql");

                sql = MessageFormat.format(sql, databaseName);

                try (ResultSet resultSet = statement.executeQuery(sql)) {

                    resultSet.next();

                    return resultSet.getBoolean("database_exists");
                }
            }
        }
    }
}
