package com.WhseApi.dao;

import com.WhseApi.entity.TypechoContentsTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TypechoContentsTaskDao {
    TypechoContentsTask getByIdAndType(@Param("contentId") Integer contentId,
                                       @Param("type") Integer type);

    void insert(@Param("task") TypechoContentsTask task);

    void update(@Param("task")TypechoContentsTask task);

    List<TypechoContentsTask> getEffective();
}
