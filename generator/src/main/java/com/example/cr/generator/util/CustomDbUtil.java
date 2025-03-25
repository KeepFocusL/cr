package com.example.cr.generator.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomDbUtil {
    public static String url = "";
    public static String user = "";
    public static String password = "";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = CustomDbUtil.url;
            String user = CustomDbUtil.user;
            String password = CustomDbUtil.password;
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 获得表注释
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static String getTableComment(String tableName) throws Exception {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select table_comment from information_schema.tables Where table_name = '" + tableName + "'");
        String tableNameCH = "";
        if (rs != null) {
            while (rs.next()) {
                tableNameCH = rs.getString("table_comment");
                break;
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("表名（中文注释）：" + tableNameCH);
        return tableNameCH;
    }

    /**
     * 获得所有列信息
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static List<Field> getColumnByTableName(String tableName) throws Exception {
        List<Field> fieldList = new ArrayList<>();
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("show full columns from `" + tableName + "`");
        if (rs != null) {
            while (rs.next()) {
                String columnName = rs.getString("Field");
                String type = rs.getString("Type");
                String comment = rs.getString("Comment");
                String nullAble = rs.getString("Null"); //YES NO
                String defaultValue = rs.getString("Default");
                Field field = new Field();
                field.setName(columnName);
                field.setNameLowerCamelCase(underLineToLowerCameCase(columnName));
                field.setNameUpperCamelCase(underLineToUpperCameCase(columnName));
                field.setType(type);
                field.setJavaType(CustomDbUtil.sqlTypeToJavaType(rs.getString("Type")));
                field.setComment(comment);
                if (comment.contains("|")) {
                    field.setNameCn(comment.substring(0, comment.indexOf("|")));
                } else {
                    field.setNameCn(comment);
                }
                field.setNullAble("YES".equals(nullAble));
                field.setDefaultValue(defaultValue);
                if (type.toUpperCase().contains("varchar".toUpperCase())) {
                    String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
                    field.setLength(Integer.valueOf(lengthStr));
                } else {
                    field.setLength(0);
                }
                if (comment.contains("枚举")) {
                    field.setEnums(true);

                    // 例：从注释中的“枚举[XxxYyyEnum]”，得到 enumsConst = XXX_YYY
                    int start = comment.indexOf("[");
                    int end = comment.indexOf("]");
                    String enumsName = comment.substring(start + 1, end); // XxxYyy
                    field.setEnumsClass(enumsName);
                    String enumsConst = StrUtil.toUnderlineCase(enumsName)
                            .toUpperCase().replace("_ENUM", "");
                    field.setEnumsConst(enumsConst);
                } else {
                    field.setEnums(false);
                }
                field.setSearchable(comment.endsWith("|searchable"));
                fieldList.add(field);
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("列信息：" + JSONUtil.toJsonPrettyStr(fieldList));
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

    /**
     * 数据库类型转为 Java 类型
     */
    public static String sqlTypeToJavaType(String sqlType) {
        if (sqlType.toUpperCase().contains("varchar".toUpperCase())
                || sqlType.toUpperCase().contains("char".toUpperCase())
                || sqlType.toUpperCase().contains("text".toUpperCase())) {
            return "String";
        } else if (sqlType.toUpperCase().contains("datetime".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("time".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("date".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("bigint".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("int".toUpperCase())) {
            return "Integer";
        } else if (sqlType.toUpperCase().contains("long".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("decimal".toUpperCase())) {
            return "BigDecimal";
        } else if (sqlType.toUpperCase().contains("boolean".toUpperCase())) {
            return "Boolean";
        } else {
            return "String";
        }
    }
}