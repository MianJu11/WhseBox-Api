package com.WhseApi.service.impl;

import com.WhseApi.dao.TypechoCashBackConfigDao;
import com.WhseApi.entity.TypechoCashBackConfig;
import com.WhseApi.entity.TypechoStandardFee;
import com.WhseApi.service.TypechoCashBackConfigService;
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
public class TypechoCashBackConfigServiceImpl implements TypechoCashBackConfigService {

    private final TypechoCashBackConfigDao dao;

    @Override
    public TypechoCashBackConfig selectByLevelGap(int levelGap) {
        return dao.selectByLevelGap(levelGap);
    }

    @Override
    public void add(TypechoCashBackConfig cashBackConfig) throws ValidationException {
        if (cashBackConfig.getId() != null) {
            cashBackConfig.setId(null);
        }
        TypechoCashBackConfig old = this.selectByLevelGap(cashBackConfig.getLevelGap());
        if (old != null) {
            throw new ValidationException("已存在该等级差");
        }
        if (cashBackConfig.getLevelGap() < 1) {
            throw new ValidationException("等级差异常");
        }
        if (cashBackConfig.getRatio() < 0 || cashBackConfig.getRatio() >= 100) {
            throw new ValidationException("百分返利比值异常");
        }
        dao.insert(cashBackConfig);
    }

    @Override
    public void update(TypechoCashBackConfig cashBackConfig) throws ValidationException {
        TypechoCashBackConfig old = this.selectByLevelGap(cashBackConfig.getLevelGap());
        if (!old.getId().equals(cashBackConfig.getId())) {
            throw new ValidationException("已存在该等级差");
        }
        if (cashBackConfig.getLevelGap() < 1) {
            throw new ValidationException("等级差异常");
        }
        if (cashBackConfig.getRatio() < 0 || cashBackConfig.getRatio() >= 100) {
            throw new ValidationException("百分返利比值异常");
        }
        dao.update(cashBackConfig);
    }

    @Override
    public void delete(Integer id) {
        dao.delete(id);
    }

    @Override
    public List<TypechoStandardFee> page(Integer page, Integer limit) {
        return dao.page((page - 1) * limit, limit);
    }

    @Override
    public int total() {
        return dao.total();
    }
}
