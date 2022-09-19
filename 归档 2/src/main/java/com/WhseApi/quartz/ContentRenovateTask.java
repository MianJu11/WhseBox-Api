package com.WhseApi.quartz;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.WhseApi.dao.TypechoContentsRenovateTaskDao;
import com.WhseApi.entity.TypechoContents;
import com.WhseApi.entity.TypechoContentsRenovateTask;
import com.WhseApi.service.TypechoContentsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.List;

/**
 * @author lty
 * 预约刷新定时任务
 */
@Slf4j
@AllArgsConstructor
public class ContentRenovateTask extends QuartzJobBean {

    private final TypechoContentsRenovateTaskDao contentsRenovateTaskDao;
    private final TypechoContentsService contentsService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("--------定时任务：预约刷新定时任务--------");
        List<TypechoContentsRenovateTask> list = contentsRenovateTaskDao.getEffective();
        Date now = new Date();
        for (TypechoContentsRenovateTask task : list) {
            // 判断是否到刷新时间
            if (task.getRenovateTime().getTime() > now.getTime()) {
                continue;
            }
            // 判断刷新次数是否还有
            if (task.getResidueTimes() == 0) {
                task.setStatus(0);
                contentsRenovateTaskDao.update(task);
                continue;
            }
            // 判断是否在刷新间隔上
            long minutes = DateUtil.between(task.getRenovateTime(), now, DateUnit.MINUTE);
            log.info("距离刷新开始时间：" + minutes);
            if (minutes % task.getIntervalTime() != 0) {
                continue;
            }
            // 刷新文章
            updateContent(task);
            // 刷新任务次数减一
            updateTask(task);
        }
    }

    private void updateTask(TypechoContentsRenovateTask task) {
        task.setResidueTimes(task.getResidueTimes() - 1);
        if (task.getResidueTimes() == 0) {
            task.setStatus(0);
        }
        contentsRenovateTaskDao.update(task);
    }

    private void updateContent(TypechoContentsRenovateTask task) {
        TypechoContents content = new TypechoContents();
        content.setCid(task.getContentId());
        content.setModified(System.currentTimeMillis()/1000);
        contentsService.update(content);
    }
}
