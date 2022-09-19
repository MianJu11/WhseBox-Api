package com.WhseApi.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserStatus {
    RedisHelp redisHelp =new RedisHelp();

    //默认用户状态，0未登录，1登陆状态，2禁用
    private Integer status = 1;
    public Integer getStatus(String token,String dataprefix, RedisTemplate redisTemplate){
        if(token==null){
            this.status=0;
            return this.status;
        }
        String key = dataprefix+"_"+"userInfo"+token;
        Map map =redisHelp.getMapValue(key,redisTemplate);
        if(map.size()==0){
            this.status=0;
            return this.status;
        }
//        Long date = System.currentTimeMillis();
//        Long old_date = (Long) redisHelp.getValue("userInfo"+token,"time",redisTemplate);
//        //清除上次数据
//        if(date - old_date > this.time){
//            redisHelp.delete("userInfo"+token,redisTemplate);
//            this.status=0;
//            return this.status;
//        }
        this.status=1;
        return this.status;
    }

    public JSONObject getUser(String token,String dataprefix, RedisTemplate redisTemplate){
        if(token==null){
            return null;
        }
        String key = dataprefix+"_"+"userInfo"+token;
        JSONObject json =redisHelp.getJson(key,redisTemplate);
        if(json.size()==0){
            return null;
        }
//        Long date = System.currentTimeMillis();
//        Long old_date = (Long) redisHelp.getValue("userInfo"+token,"time",redisTemplate);
//        //清除上次数据
//        if(date - old_date > this.time){
//            redisHelp.delete("userInfo"+token,redisTemplate);
//            this.status=0;
//            return this.status;
//        }
        return json;
    }
}
