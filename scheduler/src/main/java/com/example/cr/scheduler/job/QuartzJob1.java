package com.example.cr.scheduler.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class QuartzJob1 implements Job {
    private static final Logger log = LoggerFactory.getLogger(QuartzJob1.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("QuartzJob1.execute - start...");
        log.info("QuartzJob1.execute - end");
    }
}
