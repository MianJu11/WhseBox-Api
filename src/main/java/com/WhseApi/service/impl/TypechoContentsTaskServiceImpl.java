package com.WhseApi.service.impl;

import com.WhseApi.dao.TypechoContentsTaskDao;
import com.WhseApi.entity.TypechoContentsTask;
import com.WhseApi.service.TypechoContentsTaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lty
 */
@Service
@AllArgsConstructor
@Slf4j
public class TypechoContentsTaskServiceImpl implements TypechoContentsTaskService {

    private final TypechoContentsTaskDao dao;

    @Override
    public List<TypechoContentsTask> getEffective() {
        return dao.getEffective();
    }

    @Override
    public void update(TypechoContentsTask task) {
        dao.update(task);
    }
}
