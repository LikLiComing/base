package com.escredit.base.service;

import com.escredit.base.entity.DTO;

public interface CrudService<T, ID> {
    T save(T t);

    void delete(T t);

    T findById(ID id);

    DTO findAll(DTO dto);
}
