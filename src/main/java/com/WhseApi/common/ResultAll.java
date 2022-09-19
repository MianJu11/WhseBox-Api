package com.WhseApi.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultAll {
    public static String getResultJson(Integer code, String msg, Map data) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        return json.toString();
    }

    public static String getResultJson(Integer code, String msg, JSONObject data) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        return json.toString();
    }

    public static String getResultJson(Integer code, String msg, Object data) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", data);
        return json.toString();
    }

    public static String errorToken() {
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("msg", "用户未登录或Token验证失败");
        return json.toString();
    }

    public static String errorValidate(String msg) {
        JSONObject json = new JSONObject();
        json.put("code", 0);
        json.put("msg", msg);
        return json.toString();
    }

    public static String getListResult(Integer code, String msg, JSONArray data, int count) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", null != data ? data : new JSONArray());
        json.put("count", count);
        return json.toString();
    }

    public static String getListResult(Integer code, String msg, List data) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", null != data ? data : new ArrayList<>());
        return json.toString();
    }

    public static String getListResult(Integer code, String msg, List data, int count) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        json.put("data", null != data ? data : new ArrayList<>());
        json.put("count", count);
        return json.toString();
    }
}
