package org.endeavourhealth.dbpatcher.dataLayer;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.dataLayer.model.Function;
import org.endeavourhealth.dbpatcher.dataLayer.model.Trigger;
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

    public List<Function> getUserFunctions() throws Exception {
        try (Statement statement = createStatement()) {

            String sql = ResourceHelper.getResourceAsString("postgresql/get-user-functions.sql");

            try (ResultSet resultSet = statement.executeQuery(sql)) {

                List<Function> functions = new ArrayList<>();

                while (resultSet.next()) {
                    Function function = new Function()
                            .setFunctionSignature(resultSet.getString("function_signature"));

                    functions.add(function);
                }

                return functions;
            }
        }
    }

    public List<Trigger> getUserTriggers() throws Exception {
        try (Statement statement = createStatement()) {

            String sql = ResourceHelper.getResourceAsString("postgresql/get-user-triggers.sql");

            try (ResultSet resultSet = statement.executeQuery(sql)) {

                List<Trigger> triggers = new ArrayList<>();

                while (resultSet.next()) {
                    Trigger trigger = new Trigger()
                            .setTriggerName(resultSet.getString("trigger_name"))
                            .setType(resultSet.getString("trigger_type"))
                            .setTable(resultSet.getString("trigger_table"))
                            .setEvent(resultSet.getString("trigger_event"))
                            .setLevel(resultSet.getString("trigger_level"))
                            .setShortDescription("short_description")
                            .setDescription("description");

                    triggers.add(trigger);
                }

                return triggers;
            }
        }
    }

    public void dropUserFunctions(List<Function> functions) throws Exception {

        String sql = ResourceHelper.getResourceAsString("postgresql/drop-function.sql");

        String sqlBatch = "";

        for (Function function : functions)
            sqlBatch += MessageFormat.format(sql, function.getFunctionSignature()) + "\n";

        executeStatement(sqlBatch);
    }

    public void executeSql(String functionSql) throws SQLException {
        executeStatement(functionSql);
    }

    private void executeStatement(String sql) throws SQLException {
        if (StringUtils.isBlank(sql))
            return;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
