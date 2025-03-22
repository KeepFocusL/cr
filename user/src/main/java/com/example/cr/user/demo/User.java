package com.example.cr.user.demo;

/*
如果要自动生成这样的代码需要知道
1、类名
2、字段名
2.1、字段类型
2.2、字段注释
*/
public class User {
    /*
    唯一标识，自动递增
     */
    String id;

    /*
    建表语句中字段的注释
     */
    String name;
    String email;
    String password;
    String mobile;
    String created_at;
    String updated_at;
}
