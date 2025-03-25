package com.example.cr.generator;

import com.example.cr.generator.util.CustomDbUtil;
import com.example.cr.generator.util.CustomFreemarkerUtil;
import com.example.cr.generator.util.Field;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.*;

public class CodeGenerator {
    static String basePath = "[module]/src/main/java/com/example/cr/[module]/";
    static String pomPath ="generator/pom.xml";

    static String frontendType = "web-admin";

    static String vuePath = frontendType + "/src/views/[module]/";
    static boolean readOnly = false;

    static String apiJsPath = frontendType + "/src/api/[module]/";

    public static void main(String[] args) throws Exception {
        /*CustomFreemarkerUtil.getTemplate("test.ftl");
        Map<String, Object> data = new HashMap<>();
        String className = "Test123";
        data.put("className", className);
        CustomFreemarkerUtil.generate(toPath + className + ".java", data);*/

        String configurationFile = readConfigurationFileFromPomXml();
        // 如 generator-config-user.xml，则 module=user
        String module = configurationFile.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module=" + module);
        // 真正要生成的目标位置
        basePath = basePath.replace("[module]", module);
        new File(basePath).mkdirs();
        // new File(basePath).mkdirs();
        System.out.println("basePath: " + basePath);
        boolean isAdmin = frontendType.endsWith("admin");
        System.out.println("isAdmin=" + isAdmin);

        Document document = new SAXReader().read("generator/" + configurationFile);
        List<Node> nodes = document.selectNodes("//table");
        String domainObjectName = null;
        String tableName = null;
        for (Node table : nodes) {
            Node tableNode = table.selectSingleNode("@tableName");
            Node domainObjectNameNode = table.selectSingleNode("@domainObjectName");
            domainObjectName = domainObjectNameNode.getText();
            tableName = tableNode.getText();
            System.out.println("表名：" + tableName +"，对应的实体名："+ domainObjectName);
        }

        // --- <数据库配置信息> ---
        Node connectionRUL = document.selectSingleNode("//@connectionURL");
        Node userId = document.selectSingleNode("//@userId");
        Node password = document.selectSingleNode("//@password");
        CustomDbUtil.url = connectionRUL.getText();
        CustomDbUtil.user = userId.getText();
        CustomDbUtil.password = password.getText();
        // --- </数据库配置信息> ---

        // 假设当前项目的表名有统一前缀 xxx_ 则后续需要的各种变量尽量以实体名做为基础进行修改
        // 大驼峰格式的 Domain 变量 (配置文件中配置的实体名，如 OneTwo)
        String Domain = domainObjectName;
        // 小驼峰格式的 domain = (配置文件中配置的实体名，首字母改成小些，如 oneTwo)
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // 中划线格式的 do-main，常用于 url 路径，但是由于在 Java 中，变量名不是包含中中划线，改用 do_main
        // (配置文件中配置的实体名，每个单词的首字母都改成小写，中间用 - 连接，如 one-two)
        // 这里以表名为基础进行修改，如表名有前缀，记得先替换前缀
        String do_main = tableName.replace("xxx_", "").replaceAll("_", "-");

        // --- <数据库表和字段信息> ---
        String tableNameCn = CustomDbUtil.getTableComment(tableName);
        List<Field> fieldList = CustomDbUtil.getColumnByTableName(tableName);
        // --- </数据库表和字段信息> ---
        Set<String> typeSet = getJavaType(fieldList);

        Map<String, Object> data = new HashMap<>();
        data.put("module", module);
        data.put("moduleFirstLetter", module.substring(0, 1).toLowerCase());
        data.put("Domain", Domain);
        data.put("domain", domain);
        data.put("do_main", do_main);
        data.put("tableNameCn", tableNameCn);
        data.put("fieldList", fieldList);
        data.put("typeSet", typeSet);
        data.put("readOnly", readOnly);
        data.put("isAdmin", isAdmin);
        System.out.println(data);

        generate(Domain, data, "service", "service");
        generate(Domain, data, "controller" + (isAdmin ? "/admin" : ""), "controller");
        generate(Domain, data, "request", "request");
        generate(Domain, data, "request", "listRequest");
        generate(Domain, data, "response", "response");

        generateVue(Domain, data);

        generateApiJs(domain, data);
    }

    private static void generate(String Domain, Map<String, Object> data, String packageName, String target) throws Exception {
        CustomFreemarkerUtil.getTemplate(target + ".ftl");
        String Target = target.substring(0, 1).toUpperCase() + target.substring(1);
        String targetPath = basePath + packageName + "/";
        new File(targetPath).mkdirs();
        if ("controller".equals(target)) {
            Target = "Admin" + Target.substring(0, 1).toUpperCase() + target.substring(1);
        }
        String typeClass = targetPath + Domain + Target + ".java";
        CustomFreemarkerUtil.generate(typeClass, data);
        System.out.println("自动生成 " + Target + " 完成，" + typeClass);
    }

    private static void generateVue(String Domain, Map<String, Object> data) throws Exception {
        CustomFreemarkerUtil.getTemplate("vue.ftl");
        vuePath = vuePath.replace("[module]", String.valueOf(data.get("module")));
        new File(vuePath).mkdirs();
        String fileName = vuePath + Domain + ".vue";
        System.out.println("自动生成 " + fileName + " 完成");
        CustomFreemarkerUtil.generate(fileName, data);
    }

    private static void generateApiJs(String domain, Map<String, Object> data) throws Exception {
        CustomFreemarkerUtil.getTemplate("apiJs.ftl");
        apiJsPath = apiJsPath.replace("[module]", String.valueOf(data.get("module")));
        new File(apiJsPath).mkdirs();
        String fileName = apiJsPath + domain + ".js";
        System.out.println("自动生成 " + fileName + " 完成");
        CustomFreemarkerUtil.generate(fileName, data);
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

    /**
     * 获得所有需要被 import 的 Java 类型，通过 Set 自动去重复
     *
     * @param fieldList 字段列表
     */
    static Set<String> getJavaType(List<Field> fieldList) {
        Set<String> set = new HashSet<>();
        fieldList.forEach(field -> set.add(field.getJavaType()));
        return set;
    }
}
