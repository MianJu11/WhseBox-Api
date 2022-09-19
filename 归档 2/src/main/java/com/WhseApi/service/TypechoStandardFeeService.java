package com.WhseApi.service;

import com.WhseApi.entity.TypechoStandardFee;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface TypechoStandardFeeService {
    List<TypechoStandardFee> getStandard(Integer type);

    void add(TypechoStandardFee standardFee) throws ValidationException;

    void update(TypechoStandardFee standardFee) throws ValidationException;

    List<TypechoStandardFee> page(Integer page, Integer limit);

    int total();

    void delete(Integer id);
}
