package com.escredit.base.dao;

import java.util.List;
import java.util.Map;

public interface DaoInterface {

    /**
     * 插入数据
     *
     * @param t
     * @return
     */
    <T> int insert(T t);

    /**
     * 更新数据
     *
     * @param t
     * @return
     */
    <T> int update(T t);

    /**
     * 更新数据
     * 有只的才更新
     *
     * @param t
     * @return
     */
    <T> int updatePartial(T t);

    /**
     * 根据 id 删除单条记录
     *
     * @param id
     * @return
     */
    int deleteById(long id);

    /**
     * 根据 ids 删除多条记录
     *
     * @param ids
     * @return
     */
    int deleteByIds(long[] ids);

    /**
     * 根据ID取得详情
     *
     * @param id
     * @return
     */
    <T> T getById(long id);

    /**
     * 执行查询
     *
     * @param map
     * @return
     */
    <T> List<T> query(Map<String, Object> map);

    /**
     * 查询总数
     *
     * @param map
     * @return
     */
    long queryCount(Map<String, Object> map);
}
