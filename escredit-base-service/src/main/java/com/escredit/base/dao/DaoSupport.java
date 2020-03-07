package com.escredit.base.dao;

import java.util.List;
import java.util.Map;

public abstract class DaoSupport implements DaoInterface {

    public abstract DaoInterface getDao();

    @Override
    public <T> int insert(T t) {
        return this.getDao().insert(t);
    }

    @Override
    public <T> int update(T t) {
        return this.getDao().update(t);
    }

    @Override
    public int deleteById(long id) {
        return this.getDao().deleteById(id);
    }

    @Override
    public int deleteByIds(long[] ids) {
        return this.getDao().deleteByIds(ids);
    }

    @Override
    public <T> T getById(long id) {
        return getDao().getById(id);
    }

    @Override
    public <T> List<T> query(Map<String, Object> map) {
        return getDao().query(map);
    }

    @Override
    public long queryCount(Map<String, Object> map) {
        return getDao().queryCount(map);
    }
}
