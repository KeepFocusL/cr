package com.example.cr.generator;

import com.example.cr.generator.util.CustomDbUtil;
import com.example.cr.generator.util.CustomFreemarkerUtil;
import com.example.cr.generator.util.Field;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerator {
    static String toPath = "[module]/src/main/java/com/example/cr/[module]/demo/";

    static String pomPath ="generator/pom.xml";

    public static void main(String[] args) throws Exception {
        /*CustomFreemarkerUtil.getTemplate("test.ftl");

        String tableName = "user";
        String c = tableName.charAt(0) + "";
        System.out.println(c);
        String upperCase = c.toUpperCase();
        System.out.println(upperCase);
        String substring = tableName.substring(1);
        System.out.println(substring);
        String tableNameUpperCase = upperCase + substring;
        System.out.println(tableNameUpperCase);
        String className = tableNameUpperCase + "Service";
        System.out.println(className);

        Map<String, Object> data = new HashMap<>();
        data.put("className", className);

        CustomFreemarkerUtil.generate(toPath + className + ".java", data);*/

//        CustomFreemarkerUtil.getTemplate("test.ftl");
        CustomFreemarkerUtil.getTemplate("entity.ftl");

        String configurationFile = readConfigurationFileFromPomXml();
        System.out.println("configurationFile = " + configurationFile);
        // 动态获取当前模块名
        String module = configurationFile.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("代码要自动生产到哪个模块 = " + module);
        toPath = toPath.replace("[module]", module);
        System.out.println("动态修改后的 toPath 路径 = " + toPath);
        new File(toPath).mkdirs();

        Document document = new SAXReader().read("generator/" + configurationFile);

        // --- <数据库配置信息> ---
        Node connectionURL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        CustomDbUtil.url = connectionURL.getText();
        CustomDbUtil.user = userId.getText();
        CustomDbUtil.password = password.getText();
        System.out.println(CustomDbUtil.url);
        System.out.println(CustomDbUtil.user);
        System.out.println(CustomDbUtil.password);
        // --- </数据库配置信息> ---

        List<Node> nodes = document.selectNodes("//table");
        for (Node table : nodes) {
            Node tableNameNode = table.selectSingleNode("@tableName");
            Node domainObjectNameNode = table.selectSingleNode("@domainObjectName");

            String tableName = tableNameNode.getText();
            String domainObjectName = domainObjectNameNode.getText();

            System.out.println("表名 = " + tableName + ", 对应的实体名 = " + domainObjectName);

            List<Field> columnByTableName = CustomDbUtil.getColumnByTableName(tableName);
            System.out.println(columnByTableName);

            Map<String, Object> data = new HashMap<>();
//            String className = domainObjectName + "Service";
            String className = domainObjectName;
            data.put("className", className);
            data.put("module", module);
            data.put("fieldList", columnByTableName);

            CustomFreemarkerUtil.generate(toPath + className + ".java", data);
        }
    }

    private static String readConfigurationFileFromPomXml() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        HashMap<String, String> map = new HashMap<>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        String configurationFile = node.getText();
        System.out.println("从 pom.xml 读取 mybatis-generator-maven-plugin 需要用到的 configurationFile=" + configurationFile);
        return configurationFile;
    }
}
