package com.example.cr.user.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Value("${custom.name}")
    String customName;

    @GetMapping("test")
    public String test(){
        return "hello " + customName + " module!";
    }

    @GetMapping("test1")
    public String test1(){
        return "Test1 对前端的响应";
    }

}
