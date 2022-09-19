package com.WhseApi.dao;

import com.WhseApi.entity.TypechoContentsRenovateTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TypechoContentsRenovateTaskDao {
    List<TypechoContentsRenovateTask> getEffective();

    void insert(@Param("task") TypechoContentsRenovateTask task);

    void update(@Param("task")TypechoContentsRenovateTask task);
}
