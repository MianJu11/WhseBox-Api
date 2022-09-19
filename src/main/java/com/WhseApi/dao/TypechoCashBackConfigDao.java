package com.WhseApi.dao;

import com.WhseApi.entity.TypechoCashBackConfig;
import com.WhseApi.entity.TypechoStandardFee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TypechoCashBackConfigDao {
    TypechoCashBackConfig selectByLevelGap(@Param("levelGap") int levelGap);

    void insert(@Param("cashBackConfig") TypechoCashBackConfig cashBackConfig);

    void update(@Param("cashBackConfig") TypechoCashBackConfig cashBackConfig);

    void delete(@Param("id") Integer id);

    List<TypechoStandardFee> page(@Param("page") int page,
                                  @Param("limit") Integer limit);

    int total();
}
