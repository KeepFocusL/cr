package com.example.cr.generator.util;

public class Field {
    String name;
    String nameLowerCamelCase; // 字段名小驼峰：xxxId
    String nameUpperCamelCase; // 字段名大驼峰：XxxId
    String type;
    String javaType;
    String comment;

    public Field(String name, String type, String comment) {
        this.name = name;
        this.type = type;
        this.comment = comment;
    }

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

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", nameLowerCamelCase='" + nameLowerCamelCase + '\'' +
                ", nameUpperCamelCase='" + nameUpperCamelCase + '\'' +
                ", type='" + type + '\'' +
                ", javaType='" + javaType + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
