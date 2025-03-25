package com.example.cr.generator.util;

public class Field {
    private String name; // 字段名：xxx_id
    private String nameLowerCamelCase; // 字段名小驼峰：xxxId
    private String nameUpperCamelCase; // 字段名大驼峰：XxxId
    private String nameCn; // 中文名。例：用户
    private String type; // 字段类型。例：char(8)
    private String javaType; // java类型。例：String
    private String comment; // 注释。例：用户|其他描述
    private Boolean nullAble; // 允许为空
    private String defaultValue; // 默认值
    private Integer length; // 字符串长度
    private Boolean enums; // 是否枚举类型
    private String enumsConst; // 枚举常量。例：USER_TYPE
    private String enumsClass; // 从注释中解析出枚举类名。例：UserType
    private Boolean searchable; // 该字段在列表页面是否可被搜索

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLowerCamelCase() {
        return nameLowerCamelCase;
    }

    public void setNameLowerCamelCase(String nameLowerCamelCase) {
        this.nameLowerCamelCase = nameLowerCamelCase;
    }

    public String getNameUpperCamelCase() {
        return nameUpperCamelCase;
    }

    public void setNameUpperCamelCase(String nameUpperCamelCase) {
        this.nameUpperCamelCase = nameUpperCamelCase;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getNullAble() {
        return nullAble;
    }

    public void setNullAble(Boolean nullAble) {
        this.nullAble = nullAble;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getEnums() {
        return enums;
    }

    public void setEnums(Boolean enums) {
        this.enums = enums;
    }

    public String getEnumsConst() {
        return enumsConst;
    }

    public void setEnumsConst(String enumsConst) {
        this.enumsConst = enumsConst;
    }

    public String getEnumsClass() {
        return enumsClass;
    }

    public void setEnumsClass(String enumsClass) {
        this.enumsClass = enumsClass;
    }

    public Boolean getSearchable() {
        return searchable;
    }

    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", nameLowerCamelCase='" + nameLowerCamelCase + '\'' +
                ", nameUpperCamelCase='" + nameUpperCamelCase + '\'' +
                ", nameCn='" + nameCn + '\'' +
                ", type='" + type + '\'' +
                ", javaType='" + javaType + '\'' +
                ", comment='" + comment + '\'' +
                ", nullAble=" + nullAble +
                ", defaultValue='" + defaultValue + '\'' +
                ", length=" + length +
                ", enums=" + enums +
                ", enumsConst='" + enumsConst + '\'' +
                ", enumsClass='" + enumsClass + '\'' +
                ", searchable=" + searchable +
                '}';
    }
}