package com.WhseApi.service;

import com.WhseApi.common.PageList;
import com.WhseApi.entity.TypechoContents;
import com.WhseApi.entity.TypechoUsers;
import com.WhseApi.query.ContentQuery;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

/**
 * 业务层
 * TypechoContentsService
 * @author buxia97
 * @date 2021/11/29
 */
public interface TypechoContentsService {

    /**
     * [新增]
     **/
    int insert(TypechoContents typechoContents);

    /**
     * [批量新增]
     **/
    int batchInsert(List<TypechoContents> list);

    /**
     * [更新]
     **/
    int update(TypechoContents typechoContents);

    /**
     * [删除]
     **/
    int delete(Object key);

    /**
     * [批量删除]
     **/
    int batchDelete(List<Object> keys);

    /**
     * [主键查询]
     **/
    TypechoContents selectByKey(Object key);

    /**
     * [条件查询]
     **/
    List<TypechoContents> selectList (TypechoContents typechoContents);

    /**
     * [分页条件查询]
     **/
    PageList<TypechoContents> selectPage (TypechoContents typechoContents, Integer page, Integer pageSize,String searchKey,String order,Integer random);

    /**
     * [总量查询]
     **/
    int total(TypechoContents typechoContents);

    /**
     * 查询轮播文章
     */
    JSONArray queryRecommendList(Integer page, Integer limit, TypechoContents query);

    void buyStick(TypechoUsers uid, ContentQuery buy);

    void renovate(TypechoUsers userInfo, ContentQuery renovate);

    void updatePaymentViews();

    void updateCommonViews();
}
