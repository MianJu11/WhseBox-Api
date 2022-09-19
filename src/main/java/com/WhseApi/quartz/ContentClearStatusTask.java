package com.WhseApi.quartz;

import com.WhseApi.common.ContentStatueEnum;
import com.WhseApi.entity.TypechoContents;
import com.WhseApi.entity.TypechoContentsTask;
import com.WhseApi.service.TypechoContentsService;
import com.WhseApi.service.TypechoContentsTaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * @author lty
 * 文章状态（置顶、轮播、火急）重置
 */
@Slf4j
@AllArgsConstructor
public class ContentClearStatusTask extends QuartzJobBean {

    private final TypechoContentsTaskService contentsTaskService;
    private final TypechoContentsService contentsService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("--------定时任务：文章状态（置顶、轮播、火急）重置--------");
        List<TypechoContentsTask> list = contentsTaskService.getEffective();
        for (TypechoContentsTask task : list) {
            if (task.getEndTime() < System.currentTimeMillis()) {
                updateContent(task);
                updateTask(task);
            }
        }
    }

    private void updateTask(TypechoContentsTask task) {
        task.setStatus(0);
        contentsTaskService.update(task);
    }

    private void updateContent(TypechoContentsTask task) {
        TypechoContents content = new TypechoContents();
        content.setCid(task.getContentId());
        if (task.getType() == ContentStatueEnum.STICKY.getCode()) {
            content.setSticky(0);
        } else if (task.getType() == ContentStatueEnum.CAROUSEL.getCode()) {
            content.setCarousel(0);
        } else if (task.getType() == ContentStatueEnum.BURNING.getCode()) {
            content.setBurning(0);
        }
        contentsService.update(content);
    }
}
