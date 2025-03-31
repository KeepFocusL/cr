package com.example.cr.scheduler.job;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class SpringJobDemo {
    @Scheduled(cron = "0/5 * * * *  ?")
    public void job1(){
        System.out.println("SpringJobDemo.job1");
    }
}
