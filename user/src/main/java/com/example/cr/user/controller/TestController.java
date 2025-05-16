package com.example.cr.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {
    @Value("${custom.name}")
    String customName;

    @Autowired
    Environment environment;

    @GetMapping("test")
    public String test(){
        return "hello " + customName + " module! port = " + environment.getProperty("server.port");
    }

    @GetMapping("test1")
    public String test1(){
        return "Test1 对前端的响应";
    }

}
