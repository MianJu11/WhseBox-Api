package com.WhseApi.vo;

import com.WhseApi.entity.TypechoUsers;
import lombok.Data;

/**
 * @author lty
 */
@Data
public class UserVO {
    /**
     * 姓名
     */
    private String name;
    /**
     * vip 到期时间
     */
    private Integer vip;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户ID
     */
    private Integer uid;

    /**
     * 日期
     */
    private Integer created;
	private String mail;

    public UserVO(){}

    public UserVO(TypechoUsers user){
        this.name = user.getName();
        this.vip = user.getVip();
        this.uid = user.getUid();
        this.created = user.getCreated();
        this.avatar = user.getAvatar();
        this.mail = user.getMail();
    }
}
