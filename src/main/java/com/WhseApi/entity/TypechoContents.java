package com.WhseApi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * TypechoContents
 * @author iOSWki 2021-11-29
 */
@Data
public class TypechoContents implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * cid  
     */
    private Integer cid;

    /**
     * title  
     */
    private String title;

    /**
     * slug  
     */
    private String slug;

    /**
     * created  
     */
    private Long created;

    /**
     * modified  
     */
    private Long modified;

    /**
     * text  
     */
    private String text;

    /**
     * order  
     */
    private Integer orderKey;

    /**
     * authorId  
     */
    private Integer authorId;

    /**
     * template  
     */
    private String template;

    /**
     * type  
     */
    private String type;

    /**
     * status  
     */
    private String status;

    /**
     * password  
     */
    private String password;

    /**
     * commentsNum  
     */
    private Integer commentsNum;

    /**
     * allowComment  
     */
    private String allowComment;

    /**
     * allowPing  
     */
    private String allowPing;

    /**
     * allowFeed  
     */
    private String allowFeed;

    /**
     * parent  
     */
    private Integer parent;

    /**
     * views
     */
    private Integer views;

    /**
     * likes
     */
    private Integer likes;

    /**
     * isrecommend
     */
    private Integer isrecommend;
	
	 /**
     * isstickyzd  置顶
     */
    private Integer isstickyzd;
	 /**
     * isstickygg  公告置顶
     */
    private Integer isstickygg;
    /**
	
	/**
     * whsexb  线报
     */
    private Integer whsexb;
	
	 /**
     * whsexbrw  任务
     */
    private Integer whsexbrw;

    /**
     * logo
     */
    private String logo;
	
	/**
     * whsexburl 线报URL
     */
    private String whsexburl;
	 /**
     * whserwurl 任务URL
     */
    private String whserwurl;
	/**
     * whseprice 文章积分
     */
    private String whseprice;
    /**
     * appleipa apple
     */
    private String appleipa;

    /**
     * shopmid 获取商品ID
     */
    private String shopmid;
	 /**
     * whsevip 会员VIP折扣
     */
    private String whsevip;
    /**
     * 轮播
     */
    private Integer carousel;
    /**
     * 置顶
     */
    private Integer sticky;
    /**
     * 火急
     */
    private Integer burning;

    /**
     * 刷新标识
     */
    private Integer burningsx;

}