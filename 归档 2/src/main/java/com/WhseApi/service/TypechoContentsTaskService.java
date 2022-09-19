package com.WhseApi.service;

import com.WhseApi.entity.TypechoContentsTask;

import java.util.List;

public interface TypechoContentsTaskService {
    List<TypechoContentsTask> getEffective();

    void update(TypechoContentsTask task);
}
