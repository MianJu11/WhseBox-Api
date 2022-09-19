package com.WhseApi.common;

import com.WhseApi.dao.TypechoUsersDao;
import com.WhseApi.entity.TypechoCashBackConfig;
import com.WhseApi.entity.TypechoPaylog;
import com.WhseApi.entity.TypechoUsers;
import com.WhseApi.service.TypechoCashBackConfigService;
import com.WhseApi.service.TypechoPaylogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * 消费辅助类，在用户消费时调用此类，可以依次给上一级邀请者返现
 */
@AllArgsConstructor
@Component
public class ConsumeHelper {
    private final TypechoUsersDao usersDao;
    private final TypechoCashBackConfigService cashBackConfigService;
    private final TypechoPaylogService payLogService;

    /**
     * 用户消费时调用，可以逐层给其邀请者返利
     *
     * @param consumeUserId 消费的用户id
     * @param consumeScore  消费的积分
     */
    public void cashBack(Integer consumeUserId, Integer consumeScore) {
        TypechoUsers consumeUser = usersDao.selectByKey(consumeUserId);
        cashBack(consumeUser, consumeScore, 1);
    }

    private void cashBack(TypechoUsers consumeUser, Integer consumeScore, int levelGap) {
        if (consumeUser.getInviterId() == null) {
            return;
        }
        // 1.查询邀请者
        TypechoUsers inviter = usersDao.selectByKey(consumeUser.getInviterId());
        // 2.查询比例
        TypechoCashBackConfig cashBackConfig = cashBackConfigService.selectByLevelGap(levelGap);
        // 3.计算返回后的积分并更新
        int assets = inviter.getAssets() == null ? 0 : inviter.getAssets();
        assets += consumeScore * cashBackConfig.getRatio() / 100;
        inviter.setAssets(assets);
        usersDao.update(inviter);
        // 4.记录返现
        TypechoPaylog paylog = new TypechoPaylog();
        paylog.setStatus(1);
        Long date = System.currentTimeMillis();
        String userTime = String.valueOf(date).substring(0,10);
        paylog.setCreated(Integer.parseInt(userTime));
        paylog.setUid(inviter.getUid());
        paylog.setOutTradeNo(userTime + " - 奖励");
        paylog.setTotalAmount("+" + consumeScore * cashBackConfig.getRatio() / 100);
        paylog.setPaytype("subordinates");
        paylog.setSubject("下级消费返利");
        payLogService.insert(paylog);
        // 5.递归调用
        levelGap++;
        cashBack(inviter, consumeScore, levelGap);
    }


}
