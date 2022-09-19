package com.WhseApi.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lty
 * 定时任务配置
 */
@Configuration
public class JobConfig {

    @Bean
    public JobDetail contentClearStatusJobDetail(){
        return JobBuilder.newJob(ContentClearStatusTask.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger contentClearStatusTrigger(){
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("0 */1 * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(contentClearStatusJobDetail())
                .withSchedule(cron)
                .build();
    }

    @Bean
    public JobDetail contentRenovateJobDetail(){
        return JobBuilder.newJob(ContentRenovateTask.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger contentRenovateTrigger(){
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("0 */1 * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(contentRenovateJobDetail())
                .withSchedule(cron)
                .build();
    }

    @Bean
    public JobDetail contentIncreaseViewsJobDetail(){
        return JobBuilder.newJob(ContentIncreaseViewsTask.class)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger contentIncreaseViewsTrigger(){
        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule("0 */5 * * * ?");
        return TriggerBuilder.newTrigger()
                .forJob(contentIncreaseViewsJobDetail())
                .withSchedule(cron)
                .build();
    }

}
