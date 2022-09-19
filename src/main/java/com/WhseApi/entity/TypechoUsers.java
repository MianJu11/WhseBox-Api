package com.WhseApi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * TypechoUsers
 * @author iOSWki 2021-11-29
 */
@Data
public class TypechoUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * uid  
     */
    private Integer uid;

    /**
     * name  
     */
    private String name;

    /**
     * password  
     */
    private String password;

    /**
     * mail  
     */
    private String mail;

    /**
     * url  
     */
    private String url;

    /**
     * screenName  
     */
    private String screenName;
	
	 /**
     * userappName  
     */
    private String userappName;

    /**
     * userappNameipa
     */
    private String userappNameipa;

    /**
     * created  
     */
    private Integer created;

    /**
     * activated  
     */
    private Integer activated;

    /**
     * logged  
     */
    private Integer logged;

    /**
     * group  
     */
    private String groupKey;

    /**
     * authCode  
     */
    private String authCode;

    /**
     * introduce
     */
    private String introduce;

    /**
     * logged
     */
    private Integer assets;

    /**
     * address
     */
    private String address;

    /**
     * address
     */
    private String pay;

    /**
     * customize
     */
    private String customize;

    /**
     * vip
     */
    private Integer vip;

    /**
     * avatar
     */
    private String avatar;

    /**
     * 刷新次数
     */
    private Integer renovate;

    /**
     * 邀请者ID
     */
    private Integer inviterId;

    /**
     * 三级分销中的层级
     */
    private Integer userLevel;

    public TypechoUsers(){}

    public TypechoUsers(String _name){
        this.name = _name;
    }
}