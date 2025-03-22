package com.example.cr.generator.util;

public class Field {
    String name;
    String type;
    String comment;

    public Field(String name, String type, String comment) {
        this.name = name;
        this.type = type;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
