package com.escredit.base.service;

import com.escredit.base.entity.DTO;

import java.util.List;

public interface ServiceInterface {

    <T> DTO add(T t);

    <T> DTO update(T t);

    DTO deleteOne(long id);

    DTO deleteMultiple(long[] ids);

    DTO query(DTO dto);

    long queryCount(DTO dto);

    <T> T getById(long id);

}
