package com.escredit.base.util.collect;

import com.alibaba.fastjson.JSONArray;
import com.escredit.base.util.reflect.ReflectUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义树
 */
public class DefinitionTree{

    private TreeProperties treeProperties;

    public DefinitionTree() {
        this.treeProperties = new TreeProperties();
    }

    public DefinitionTree(TreeProperties treeProperties) {
        this.treeProperties = treeProperties;
    }

    public List generateJSONTree(Collection sourceList){
        String parentName = treeProperties.getParentidName();
        List rootList = new ArrayList();
        List childList = new ArrayList();
        sourceList.stream().forEach(item ->{
            if(ReflectUtils.invokeGetter(item,parentName) == null || (Long)ReflectUtils.invokeGetter(item,parentName) == 0){
                rootList.add(item);
            }else{
                childList.add(item);
            }
        });
        List treeList = new ArrayList();
        rootList.stream().forEach(item ->{
            packageTreeList(treeList,item,childList);
        });
        return treeList;
    }

    /**
     * 生成树
     * @param sourceList
     * @return
     */
    public String generateJSONTreeString(Collection sourceList){
        return JSONArray.toJSONString(generateJSONTree(sourceList));
    }

    private void packageTreeList(List treeList, Object item, List childList){
        try {
            Object treeitem = item.getClass().newInstance();
            BeanUtils.copyProperties(item,treeitem);
            String idName = treeProperties.getIdName();
            String childrenName = treeProperties.getChildrenName();
            List childRole = getChildRole(ReflectUtils.invokeGetter(item, idName), childList);
            ReflectUtils.setFieldValue(treeitem,childrenName,childRole);
            treeList.add(treeitem);
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private List getChildRole(Long parentid,List childList){
        List treeList = new ArrayList();
        String parentName = treeProperties.getParentidName();
        childList.stream().forEach(item->{
            if(!parentid.equals(ReflectUtils.invokeGetter(item,parentName))){
                return;
            }
            packageTreeList(treeList,item,childList);
        });
        return treeList;
    }

    public static class VueTree {

        private Long id;

        private Long parentid;

        private String code;

        private String label;

        private List<VueTree> children;

        private Object object;

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public Long getParentid() {
            return parentid;
        }

        public void setParentid(Long parentid) {
            this.parentid = parentid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public List<VueTree> getChildren() {
            return children;
        }

        public void setChildren(List<VueTree> children) {
            this.children = children;
        }
    }


}
