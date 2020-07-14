package com.escredit.base.service;

import com.escredit.base.dao.DaoInterface;
import com.escredit.base.entity.DTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ServiceSupport implements ServiceInterface{

    protected abstract DaoInterface getDao();

    @Override
    public <T> DTO add(T t) {
        try {
            DTO dto = beforeAdd(t);
            //校验
            if(!dto.isSuccess()){
                return dto;
            }
            int affected = getDao().insert(t);
            return dto.setSuccess(affected !=-1 ).putResult("affected", affected).setObject(t);
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport add error", e);
        }
    }

    protected <T> DTO beforeAdd(T t) {
        DTO dto = new DTO(true);
        return dto.setObject(t);
    }

    @Override
    public <T> DTO update(T t) {
        try {
            DTO dto = beforeModify(t);
            //校验
            if(!dto.isSuccess()){
                return dto;
            }
            int affected = getDao().update(t);
            return dto.setSuccess(affected !=-1 ).putResult("affected", affected).setObject(t);
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport modify error", e);
        }
    }

    @Override
    public <T> DTO updatePartial(T t) {
        try {
            DTO dto = beforeModify(t);
            //校验
            if(!dto.isSuccess()){
                return dto;
            }
            int affected = getDao().updatePartial(t);
            return dto.setSuccess(affected !=-1 ).putResult("affected", affected).setObject(t);
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport modify error", e);
        }
    }

    protected <T> DTO beforeModify(T t) {
        DTO dto = new DTO(true);
        return dto.setObject(t);
    }

    @Override
    public DTO deleteOne(long id) {
        try {
            return new DTO(true).putResult("affected", getDao().deleteById(id));// 如果返回0，表示数据未被删除
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport deleteOne error", e);
        }
    }

    @Override
    public DTO deleteMultiple(long[] ids) {
        try {
            return new DTO(true).putResult("affected", getDao().deleteByIds(ids));// 如果返回0，表示数据未被删除
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport deleteMultiple error", e);
        }
    }

    @Override
    public <T> T getById(long id) {
        try {
            return afterGet((T) getDao().getById(id));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport getById error", e);
        }
    }

    protected <T> T afterGet(T t) {
        return t;
    }

    @Override
    public DTO query(DTO dto) {
        if (dto == null){
            throw new ServiceException("Parameter dto can't be null");
        }

        try {

            if (dto.getPage() != null){
                dto.getPage().setTotalCount(this.queryCount(dto));
            }

            dto.setList(afterQuery(getDao().query(parseParams(dto))));
            if(dto.getList() != null && dto.getList().size() != 0){
                dto.setSuccess(true);
            }
            return dto;
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport query error", e);
        }
    }

    protected <T> List<T> afterQuery(List<T> list) {
        return list;
    }

    @Override
    public long queryCount(DTO dto) {
        try {
            return getDao().queryCount(parseParams(dto));
        } catch (Exception e) {
            throw new ServiceException("ServiceSupport queryCount error", e);
        }
    }

    public static Map<String, Object> parseParams(DTO dto) {
        if (dto == null)
            return null;

        Map<String, Object> map = new HashMap<String, Object>();
        if (dto.getParamsMap() != null)
            map.putAll(dto.getParamsMap());
        if (dto.getSort() != null)
            map.put("order", dto.getSort().getOrder());
        else
            map.put("order", "");

        if (dto.getPage() != null) {
            map.put("pageTag", 1);
            map.put("position", dto.getPage().getPosition());
        } else {
            map.put("pageTag", 0);
            map.put("position", "");
        }
        return map;
    }
}
