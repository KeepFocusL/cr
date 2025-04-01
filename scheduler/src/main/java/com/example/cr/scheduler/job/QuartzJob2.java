package com.example.cr.scheduler.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class QuartzJob2 implements Job {
    private static final Logger log = LoggerFactory.getLogger(QuartzJob2.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("QuartzJob2.execute - start...");
        log.info("QuartzJob2.execute - end");
    }
}
