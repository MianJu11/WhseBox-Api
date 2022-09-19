package com.WhseApi.entity;

import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * TypechoShop
 * @author iOSWki 2022-01-27
 */
@Data
public class TypechoShop implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id  
     */
    private Integer id;

    /**
     * title  商品标题
     */
    private String title;

    /**
     * imgurl  商品图片
     */
    private String imgurl;

    /**
     * text  商品内容
     */
    private String text;

    /**
     * price  商品价格
     */
    private Integer price;
	
	
	
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
     * sofem  系统
     */
    private String sofem;
	/**
     * sofename  软件大小
     */
    private String sofename;
    /**
     * zfappName  转发仓库名称
     */
    private String zfappName;
	

    /**
     * num  商品数量
     */
    private Integer num;

    /**
     * type  商品类型（实体，源码，工具，教程）
     */
    private Integer type;

    /**
     * value  收费显示（除实体外，这个字段购买后显示）
     */
    private String value;

    /**
     * cid  所属文章
     */
    private Integer cid;

    /**
     * uid  发布人
     */
    private Integer uid;

    /**
     * created  创建时间
     */
    private Integer created;

    /**
     * status  审核状态
     */
    private Integer status;
    /**
     * vipDiscount  VIP折扣
     */
    private String vipDiscount;

}