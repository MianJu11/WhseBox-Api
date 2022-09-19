package com.WhseApi.service;

import com.WhseApi.entity.TypechoCashBackConfig;
import com.WhseApi.entity.TypechoStandardFee;

import javax.xml.bind.ValidationException;
import java.util.List;

public interface TypechoCashBackConfigService {
    TypechoCashBackConfig selectByLevelGap(int levelGap);

    void add(TypechoCashBackConfig cashBackConfig) throws ValidationException;

    void update(TypechoCashBackConfig cashBackConfig) throws ValidationException;

    void delete(Integer id);

    List<TypechoStandardFee> page(Integer page, Integer limit);

    int total();
}
