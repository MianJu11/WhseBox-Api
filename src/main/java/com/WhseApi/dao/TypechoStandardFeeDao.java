package com.WhseApi.dao;

import com.WhseApi.entity.TypechoStandardFee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TypechoStandardFeeDao {
    List<TypechoStandardFee> getStandard(@Param("type") Integer type);

    TypechoStandardFee getById(@Param("id") Integer id);

    void insert(@Param("standardFee") TypechoStandardFee standardFee);

    void update(@Param("standardFee") TypechoStandardFee standardFee);

    List<TypechoStandardFee> page(@Param("page") Integer page,
                                  @Param("limit") Integer limit);

    int total();

    int delete(@Param("id")Integer id);
}
