
package com.escredit.base.web;

import com.escredit.base.entity.DTO;
import com.escredit.base.entity.Errcode;
import com.escredit.base.entity.PageIO;
import com.escredit.base.util.collect.MapBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public abstract class WebSupport {

    private final static Logger logger = LoggerFactory.getLogger(WebSupport.class);

    protected final static String SUCCESS = "success";
    protected final static Integer SUCCESS_CODE = 0;
    protected final static String ERROR_CODE = "errcode";
    protected final static String ERROR_TEXT = "errmsg";
    protected final static String ERROR_UNKNOWN = "未知错误";

    protected final static String DEFAULT_CHARSET_NAME = "UTF-8";

    @Autowired(required = false)
    private Errcode errcode;

    protected Map<?, ?> putValue(Map<Object, Object> map, String key, Object value) {
        if (map != null && key != null) {
            map.put(key, value);
        }
        return map;
    }

    protected Map<?, ?> buildResult(DTO dto) {
        if (dto == null) {
            dto = new DTO().putResult("10", "操作数据出错");
        }
        MapBuilder mb = MapBuilder.newInstance().put(SUCCESS, dto.isSuccess());

        if (dto.isSuccess()){
            parseSuccess(mb, dto);
        }
        else{
            parseError(mb, dto);
        }

        return mb.build();
    }

    private void parseError(MapBuilder mapBuilder, DTO dto) {
        if (mapBuilder == null || dto == null){
            return;
        }
        putError(mapBuilder.build(), dto.getErrcode(),dto.getErrmsg());
    }

    protected Map<?, ?> putError(Map map, String value,String text) {
        if (map != null && value != null) {
            putValue(map, ERROR_CODE, Integer.valueOf(value));
            putValue(map, ERROR_TEXT, StringUtils.isEmpty(text)?errcode.getError().get(value):text);
        }
        return map;
    }

    private <T> void parseSuccess(MapBuilder mapBuilder, DTO dto) {
        // 1. 解析对象
        parseSuccessObject(mapBuilder, dto.getObject());
        // 2. 解析分页
        parsePageBuilder(mapBuilder, dto.getPage());
        // 3. 解析列表
        parseSuccessList(mapBuilder, dto.getList());
        // 4. 解析结果
        parseSuccessResult(mapBuilder, dto.getResultMap());

        putError(mapBuilder.build(), String.valueOf(SUCCESS_CODE),SUCCESS);
    }

    private void parseSuccessObject(MapBuilder mapBuilder, Object object) {
        if (object != null){
            mapBuilder.put("object", object);
        }
    }

    private void parseSuccessList(MapBuilder mapBuilder, List<?> list) {
        if (list != null){
            mapBuilder.put("list", list);
        }
    }

    private void parseSuccessResult(MapBuilder mapBuilder, Map<?, ?> map) {
        if (map != null){
            mapBuilder.put("result", map);
        }
    }

    private void parsePageBuilder(MapBuilder mapBuilder, PageIO pageIO) {
        if (pageIO == null){
            return;
        }
        mapBuilder.put("page", pageIO.getPage());
        mapBuilder.put("totalCount", pageIO.getTotalCount());
        mapBuilder.put("pageSize", pageIO.getPageSize());
        mapBuilder.put("totalPage", pageIO.getTotalPage());
        mapBuilder.put("offset", pageIO.getOffset());
    }
}
