
package com.escredit.base.util.collect;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class MapBuilder {

    private Map<Object, Object> map = new LinkedHashMap<Object, Object>();

    private MapBuilder(){

    }

    private MapBuilder(Map<?, ?> map){
        this.map = (Map<Object, Object>) map;
    }

    public static MapBuilder newInstance() {
        return new MapBuilder();
    }

    public static MapBuilder newInstance(Map<?, ?> map) {
        return new MapBuilder(map);
    }

    public MapBuilder put(Object key, Object value) {
        this.map.put(key, value);
        return this;
    }

    public MapBuilder putMap(Map<?, ?> map) {
        this.map.putAll(map);
        return this;
    }

    public MapBuilder pubArray(Object key, List<?> list) {
        this.map.put(key, list);
        return this;
    }

    public Map<?, ?> build() {
        return map;
    }
}
