package com.WhseApi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * TypechoApiconfig
 * @author apiconfig 2022-04-28
 */
@Data
public class TypechoApiconfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id  
     */
    private Integer id;

    /**
     * webinfoTitle  网站名称
     */
    private String webinfoTitle;

    /**
     * webinfoUrl  网站URL
     */
    private String webinfoUrl;

    /**
     * webinfoUploadUrl  本地图片访问路径
     */
    private String webinfoUploadUrl;

    /**
     * webinfoAvatar  头像源
     */
    private String webinfoAvatar;

    /**
     * pexelsKey  图库key
     */
    private String pexelsKey;

    /**
     * scale  一元能买多少积分
     */
    private Integer scale;

    /**
     * clock  签到最多多少积分
     */
    private Integer clock;

    /**
     * vipPrice  VIP一天价格
     */
    private Integer vipPrice;
	
	/**
     * adsxili  信息流广告ID（默认关闭）
     */
    private Integer adsxili;
	/**
     * mianadszl  激励视频ID（默认关闭）
     */
    private Integer mianadszl;
	
	/**
     * miangg  公告
     */
    private String miangg;
	
	/**
     * miandhlname  导航1栏名称
     */
    private String miandhlname;
	
	/**
     * miandhlurl  导航1栏链接（默认关闭）
     */
    private String miandhlurl;
	
	/**
     * mianqq  QQ群链接（默认关闭）
     */
    private String mianqq;
	
	/**
     * miankime  卡密购买（默认关闭）
     */
    private String miankime;
	
	/**
     * mianappa  私有仓库
     */
    private String mianappa;
	
	/**
     * mianappb  公有仓库
     */
    private String mianappb;
	
	/**
     * mianappc  站长微信
     */
    private String mianappc;

    /**
     * vipDay  多少天VIP等于永久
     */
    private Integer vipDay;

    /**
     * vipDiscount  VIP折扣
     */
    private String vipDiscount;

    /**
     * isEmail  是否开启邮箱注册（关闭后不再验证邮箱）
     */
    private Integer isEmail;

    /**
     * isInvite  注册是否验证邀请码（默认关闭）
     */
    private Integer isInvite;

    /**
     * versionCode  最新版本号
     */
    private String versionCode;

    /**
     * versionUrl  APP下载URL
     */
    private String versionUrl;

    /**
     * version  最新版本名称
     */
    private String version;

    /**
     * versionIntro  版本内容
     */
    private String versionIntro;

    /**
     * cosAccessKey  
     */
    private String cosAccessKey;

    /**
     * cosSecretKey  
     */
    private String cosSecretKey;

    /**
     * cosBucket  
     */
    private String cosBucket;

    /**
     * cosBucketName  
     */
    private String cosBucketName;

    /**
     * cosPath  
     */
    private String cosPath;

    /**
     * cosPrefix  
     */
    private String cosPrefix;

    /**
     * aliyunEndpoint  
     */
    private String aliyunEndpoint;

    /**
     * aliyunAccessKeyId  
     */
    private String aliyunAccessKeyId;

    /**
     * aliyunAccessKeySecret  
     */
    private String aliyunAccessKeySecret;

    /**
     * aliyunAucketName  
     */
    private String aliyunAucketName;

    /**
     * aliyunUrlPrefix  
     */
    private String aliyunUrlPrefix;

    /**
     * aliyunFilePrefix  
     */
    private String aliyunFilePrefix;

    /**
     * ftpHost  
     */
    private String ftpHost;

    /**
     * ftpPort  
     */
    private Integer ftpPort;

    /**
     * ftpUsername  
     */
    private String ftpUsername;

    /**
     * ftpPassword  
     */
    private String ftpPassword;

    /**
     * ftpBasePath  
     */
    private String ftpBasePath;

    /**
     * alipayAppId  
     */
    private String alipayAppId;

    /**
     * alipayPrivateKey  
     */
    private String alipayPrivateKey;

    /**
     * alipayPublicKey  
     */
    private String alipayPublicKey;

    /**
     * alipayNotifyUrl  
     */
    private String alipayNotifyUrl;

    /**
     * appletsAppid  
     */
    private String appletsAppid;

    /**
     * appletsSecret  
     */
    private String appletsSecret;

    /**
     * qqAppletsAppid
     */
    private String qqAppletsAppid;

    /**
     * qqAppletsSecret
     */
    private String qqAppletsSecret;

    /**
     * wxpayAppId  
     */
    private String wxpayAppId;

    /**
     * wxpayMchId  
     */
    private String wxpayMchId;

    /**
     * wxpayKey  
     */
    private String wxpayKey;

    /**
     * wxpayNotifyUrl  
     */
    private String wxpayNotifyUrl;

    /**
     * adAwardPoint
     */
    private Integer adAwardPoint;

    /**
     * adAwardPerDay  激励视频可领取次数
     */
    private Integer adAwardPerDay;

    /**
     * adRwPerDay  任务可领取次数
     */
    private Integer adRwPerDay;

    /**
     * app检测主包名action
     */
    private String bm0shifou0action;

    /**
     * app检测主包名名称
     */
    private String bm0shifou0pname;

    /**
     * app检测副包名action
     */
    private String bm0jiance0action;

    /**
     * app检测副包名名称，分号分隔
     */
    private String bm0jiance0pname;

    /**
     * app检测下载链接
     */
    private String bm0xiazai;

    /**
     * app检测提示内容
     */
    private String bm0neirong;

    /**
     * 所有软件弹窗（每天次数）
     */
    private String gg0cishu;

    /**
     * 所有软件弹窗提示内容
     */
    private String gg0neirong;

    /**
     * 所有软件弹窗提示网址
     */
    private String gg0lianjie;

	/**
     * 酱爆whseApp主程序检测更新
     */
    private String whseApp;
	/**
     * 酱爆whseApi主程序检测更新
     */
    private String whseApi;
	/**
     * 酱爆Whse仓库域名
     */
    private String whseApphttp;
	/**
     * 购买轮播价格
     */
    private Integer carouselPrice;

    /**
     * 购买火急价格
     */
    private Integer burningPrice;

    /**
     * 购买置顶价格
     */
    private Integer stickyPrice;

    /**
     * 每天最多可获取的免费刷新次数上限
     */
    private Integer freeRenovateTimes;


    /**
     * epayUrl
     */
    private String epayUrl;

    /**
     * epayPid
     */
    private Integer epayPid;

    /**
     * epayKey
     */
    private String epayKey;

    /**
     * epayNotifyUrl
     */
    private String epayNotifyUrl;
	
	/**
     * auditlevel
     */
    private Integer auditlevel;

    private String forbidden;

}