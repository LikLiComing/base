package com.escredit.base.util.collect;

/**
 * 树节点定义
 */
public class TreeProperties {

    private String idName = "id";

    private String labelName = "label";

    private String childrenName = "children";

    private String parentidName = "parentid";

    public String getParentidName() {
        return parentidName;
    }

    public void setParentidName(String parentidName) {
        this.parentidName = parentidName;
    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getChildrenName() {
        return childrenName;
    }

    public void setChildrenName(String childrenName) {
        this.childrenName = childrenName;
    }
}
