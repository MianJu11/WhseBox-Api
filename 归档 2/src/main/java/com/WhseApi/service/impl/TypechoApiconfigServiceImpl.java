package com.WhseApi.service.impl;

import com.WhseApi.entity.*;
import com.WhseApi.common.PageList;
import com.WhseApi.dao.*;
import com.WhseApi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务层实现类
 * TypechoApiconfigServiceImpl
 * @author apiconfig
 * @date 2022/04/28
 */
@Service
public class TypechoApiconfigServiceImpl implements TypechoApiconfigService {

    @Autowired
	TypechoApiconfigDao dao;

    @Override
    public int insert(TypechoApiconfig typechoApiconfig) {
        return dao.insert(typechoApiconfig);
    }


    @Override
    public int update(TypechoApiconfig typechoApiconfig) {
    	return dao.update(typechoApiconfig);
    }


	@Override
	public TypechoApiconfig selectByKey(Object key) {
		return dao.selectByKey(key);
	}

}