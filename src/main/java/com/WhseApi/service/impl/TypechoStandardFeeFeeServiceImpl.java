package com.WhseApi.service.impl;

import com.WhseApi.dao.TypechoStandardFeeDao;
import com.WhseApi.entity.TypechoStandardFee;
import com.WhseApi.service.TypechoStandardFeeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.List;

/**
 * @author lty
 */
@Service
@AllArgsConstructor
@Slf4j
public class TypechoStandardFeeFeeServiceImpl implements TypechoStandardFeeService {

    private final TypechoStandardFeeDao dao;

    @Override
    public List<TypechoStandardFee> getStandard(Integer type) {
        return dao.getStandard(type);
    }

    @Override
    public void add(TypechoStandardFee standardFee) throws ValidationException {
        if (standardFee.getId() != null) {
            standardFee.setId(null);
        }
        if (standardFee.getType() < 1 || standardFee.getType() > 4) {
            throw new ValidationException("类型异常");
        }
        if (standardFee.getPrice() < 1) {
            throw new ValidationException("天数/次数异常");
        }
        if (standardFee.getPrice() < 0) {
            throw new ValidationException("价格异常");
        }
        dao.insert(standardFee);
    }

    @Override
    public void update(TypechoStandardFee standardFee) throws ValidationException {
        TypechoStandardFee old = dao.getById(standardFee.getId());
        if (old == null) {
            throw new ValidationException("ID异常，查无此数据");
        }
        if (standardFee.getType() < 1 || standardFee.getType() > 4) {
            throw new ValidationException("类型异常");
        }
        if (standardFee.getPrice() < 1) {
            throw new ValidationException("天数/次数异常");
        }
        if (standardFee.getPrice() < 0) {
            throw new ValidationException("价格异常");
        }
        dao.update(standardFee);
    }

    @Override
    public List<TypechoStandardFee> page(Integer page, Integer limit) {
        return dao.page((page - 1) * limit, limit);
    }

    @Override
    public int total() {
        return dao.total();
    }

    @Override
    public void delete(Integer id) {
        dao.delete(id);
    }
}
