package org.endeavourhealth.dbpatcher.dataLayer;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class DataLayer {
    private Connection connection;

    public DataLayer(Connection connection) {
        this.connection = connection;
    }

    private Statement createStatement() throws SQLException {
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(10);
        return statement;
    }

    public List<String> getUserFunctionSignatures() throws Exception {
        try (Statement statement = createStatement()) {

            String sql = ResourceHelper.getResourceAsString("postgresql/get-user-functions.sql");

            try (ResultSet resultset = statement.executeQuery(sql)) {

                List<String> functions = new ArrayList<>();

                while (resultset.next())
                    functions.add(resultset.getString("proc_name"));

                return functions;
            }
        }
    }

    public void dropUserFunctions(List<String> functionSignatures) throws Exception {

        String sql = ResourceHelper.getResourceAsString("postgresql/drop-function.sql");

        String sqlBatch = "";

        for (String functionSignature : functionSignatures)
            sqlBatch += MessageFormat.format(sql, functionSignature) + "\n";

        if (StringUtils.isBlank(sqlBatch))
            return;

        executeStatement(sqlBatch);
    }

    public void executeSql(String functionSql) throws SQLException {
        executeStatement(functionSql);
    }

    private void executeStatement(String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
