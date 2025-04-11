package com.example.cr.scheduler.job;

import com.example.cr.scheduler.feign.BusinessFeign;
import jakarta.annotation.Resource;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class DailyTrainJob implements Job {
    private static final Logger log = LoggerFactory.getLogger(DailyTrainJob.class);

    @Resource
    BusinessFeign businessFeign;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("生成每日数据 - 开始");
        businessFeign.test();
        // 思考: 真正生成每日车次的步骤，流程
        log.info("生成每日数据 - 结束");
    }
}
