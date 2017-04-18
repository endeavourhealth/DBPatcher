package org.endeavourhealth.dbpatcher;

import org.apache.commons.lang3.StringUtils;
import org.endeavourhealth.dbpatcher.helpers.ResourceHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataLayer {
    private Connection connection;

    public DataLayer(Connection connection) {
        this.connection = connection;
    }

    public List<String> getUserFunctionSignatures() throws IOException, SQLException {
        try (Statement statement = connection.createStatement()) {

            String sql = ResourceHelper.getResourceAsString("postgresql/get-user-functions.sql");

            try (ResultSet resultset = statement.executeQuery(sql)) {

                List<String> functions = new ArrayList<>();

                while (resultset.next())
                    functions.add(resultset.getString("proc_name"));

                return functions;
            }
        }
    }

    public void dropUserFunctions(List<String> functionSignatures) throws SQLException {

        if (functionSignatures.size() == 0)
            return;

        String sql = StringUtils
                .join(functionSignatures
                        .stream()
                        .map(t -> "drop function " + t)
                        .collect(Collectors.toList())
               , "\n");

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }
}
