package com.WhseApi.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lty
 */
@Data
public class TypechoContentsRenovateTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 文章ID
     */
    private Integer contentId;

    /**
     * 刷新时间
     */
    private Date renovateTime;

    /**
     * 间隔时间（分钟）
     */
    private Integer intervalTime;

    /**
     * 刷新次数
     */
    private Integer renovateTimes;

    /**
     * 剩余刷新次数
     */
    private Integer residueTimes;

    /**
     * 1-生效；0-失效
     */
    private Integer status;
}