package com.example.cr.generator.util;

import java.sql.*;

public class CustomDbUtil {
    public static String url;
    public static String user;
    public static String password;

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static void getColumnByTableName(String table) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String sql = "show full columns from `" + table + "`";
        ResultSet rs= statement.executeQuery(sql);
        while (rs.next()){
            String field = rs.getString("Field");
            String type = rs.getString("Type");
            String comment = rs.getString("Comment");
            System.out.println(field + " - " + type + " - " + comment);
        }
    }
}
