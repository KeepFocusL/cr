package com.example.cr.business.controller;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.EventListener;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    Environment environment;
    @GetMapping("")
    public String test(){
        return "hello business module! port = " + environment.getProperty("server.port");
    }

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Resource
    RedisTemplate redisTemplate;

    @GetMapping("redis/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        log.info("set - key={}, value={}", key, value);
        return "success";
    }

    @GetMapping("redis/get/{key}")
    public Object get(@PathVariable String key) {
        Object object = redisTemplate.opsForValue().get(key);
        log.info("get - key={}, value={}", key, object);
        return object;
    }
}
