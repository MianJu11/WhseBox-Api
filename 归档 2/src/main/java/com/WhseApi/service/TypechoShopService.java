package com.WhseApi.service;

import java.util.Map;
import java.util.List;
import com.WhseApi.entity.*;
import com.WhseApi.common.PageList;

/**
 * 业务层
 * TypechoShopService
 * @author buxia97
 * @date 2022/01/27
 */
public interface TypechoShopService {

    /**
     * [新增]
     **/
    int insert(TypechoShop typechoShop);

    /**
     * [批量新增]
     **/
    int batchInsert(List<TypechoShop> list);

    /**
     * [更新]
     **/
    int update(TypechoShop typechoShop);

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
    TypechoShop selectByKey(Object key);

    /**
     * [条件查询]
     **/
    List<TypechoShop> selectList (TypechoShop typechoShop);

    /**
     * [分页条件查询]
     **/
    PageList<TypechoShop> selectPage (TypechoShop typechoShop, Integer page, Integer pageSize,String searchKey);

    /**
     * [总量查询]
     **/
    int total(TypechoShop typechoShop);
}
