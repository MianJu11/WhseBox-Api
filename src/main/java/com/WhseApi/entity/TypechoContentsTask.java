package com.WhseApi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lty
 */
@Data
public class TypechoContentsTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 文章ID
     */
    private Integer contentId;

    /**
     * 1-置顶；2-轮播；3-火急
     */
    private Integer type;

    /**
     * 终止时间
     */
    private Long endTime;

    /**
     * 1-生效；0-失效
     */
    private Integer status;
}