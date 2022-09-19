package com.WhseApi.entity;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * TypechoPaykey
 * @author paykey 2022-04-20
 */
@Data
public class TypechoPaykey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id  
     */
    private Integer id;

    /**
     * value  密钥
     */
    private String value;
	/**
     * sodownb  公有软件仓库
     */
    private String sodownb;
	/**
     * sodown  语言
     */
    private String sodown;
	/**
     * sodownc  私有软件仓库
     */
    private String sodownc;
	
	/**
     * downapp  下载地址2
     */
    private String downapp;
	
	/**
     * downappb  下载地址3
     */
    private String downappb;
	
    /**
     * price  等值积分
     */
    private Integer price;

    /**
     * status  0未使用，1已使用
     */
    private Integer status;

    /**
     * created  创建时间
     */
    private Integer created;

    /**
     * uid  使用用户
     */
    private Integer uid;


}