package com.WhseApi.common;

/**
 * @author lty
 * 文章状态枚举类
 */

public enum ContentStatueEnum {
    STICKY(1, "置顶"),
    CAROUSEL(2, "轮播"),
    BURNING(3, "火急");


    private int code;
    private String desc;

    private ContentStatueEnum(int _code, String _desc) {
        this.code = _code;
        this.desc = _desc;
    }

    public int getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }

    public ContentStatueEnum get(int code){
        for(ContentStatueEnum statue:ContentStatueEnum.values()){
            if(statue.getCode()==code){
                return statue;
            }
        }
        return null;
    }
}
