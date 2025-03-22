package com.example.cr.generator.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomDbUtil {
    public static String url;
    public static String user;
    public static String password;

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static List<Field> getColumnByTableName(String table) throws SQLException {
        List<Field> fieldList = new ArrayList<>();

        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        String sql = "show full columns from `" + table + "`";
        ResultSet rs= statement.executeQuery(sql);
        while (rs.next()){
            String name = rs.getString("Field");
            String type = rs.getString("Type");
            String comment = rs.getString("Comment");

            Field f = new Field(name, type, comment);
            f.setNameLowerCamelCase(underLineToLowerCameCase(name));
            f.setNameUpperCamelCase(underLineToUpperCameCase(name));
            fieldList.add(f);
        }
        return fieldList;
    }

    /**
     * 下划线转小驼峰。例：user_id 转成 userId
     */
    public static String underLineToLowerCameCase(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转大驼峰：user_id 转成 UserId
     */
    public static String underLineToUpperCameCase(String str) {
        String s = underLineToLowerCameCase(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
