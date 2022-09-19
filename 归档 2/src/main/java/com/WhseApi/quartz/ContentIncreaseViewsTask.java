package com.WhseApi.quartz;

import cn.hutool.core.date.DateUtil;
import com.WhseApi.service.TypechoContentsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

/**
 * @author lty
 * 文章增加浏览量定时任务
 */
@Slf4j
@AllArgsConstructor
public class ContentIncreaseViewsTask extends QuartzJobBean {

    private final TypechoContentsService contentsService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("--------定时任务：文章增加浏览量--------");
        contentsService.updatePaymentViews();
        int minute = DateUtil.minute(new Date());
        if(minute==0){
            contentsService.updateCommonViews();
        }

    }


}
