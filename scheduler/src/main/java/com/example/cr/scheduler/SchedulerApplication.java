package com.example.cr.scheduler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
    @ComponentScan("com.example.cr")
    @MapperScan("com.example.cr.scheduler.mapper")
    @EnableFeignClients("com.example.cr.scheduler.feign")
    public class SchedulerApplication {
        public static void main(String[] args) {
            SpringApplication.run(SchedulerApplication.class, args);
        }
    }

