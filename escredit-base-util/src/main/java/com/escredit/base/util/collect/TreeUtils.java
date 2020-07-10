package com.escredit.base.util.collect;

import com.alibaba.fastjson.JSONArray;
import com.escredit.base.util.reflect.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liyongping on 2020/2/20 5:08 PM
 */
public class TreeUtils {

    public static String generateJSONTree(Collection sourceList, String fieldName4Text,String fieldName4Parent, List checkedId, boolean selected, boolean expanded){
        String parent = StringUtils.isEmpty(fieldName4Parent)?"parentid":fieldName4Parent;
        List rootList = new ArrayList();
        List childList = new ArrayList();
        sourceList.stream().forEach(item ->{
            if(ReflectUtils.invokeGetter(item,parent) == null || (Long)ReflectUtils.invokeGetter(item,parent) == 0){
                rootList.add(item);
            }else{
                childList.add(item);
            }
        });
        List<Tree> treeList = new ArrayList();
        rootList.stream().forEach(item ->{
            packageTreeList(treeList,item,childList,fieldName4Text,checkedId,selected,expanded);
        });
        return JSONArray.toJSONString(treeList);
    }

    /**
     * 生成json树
     * @param sourceList 数据源
     * @param fieldName4Text text对应属性值
     * @return
     */
    public static String generateJSONTree(Collection sourceList, String fieldName4Text){
        return generateJSONTree(sourceList,fieldName4Text,null,null,false,true);
    }

    public static String generateJSONTree(Collection sourceList, String fieldName4Text,String fieldName4Parent){
        return generateJSONTree(sourceList,fieldName4Text,fieldName4Parent,null,false,true);
    }

    public static String generateJSONTree(Collection sourceList, String fieldName4Text, List checkedId){
        return generateJSONTree(sourceList,fieldName4Text,null,checkedId,false,true);
    }

    public static String generateJSONTree(Collection sourceList, String fieldName4Text, List checkedId, Boolean selected, Boolean expanded){
        return generateJSONTree(sourceList,fieldName4Text,null,checkedId, selected, expanded);
    }

    private static void packageTreeList(List<Tree> treeList,Object item,List childList,String fieldName4Text, List checkedId, boolean selected, boolean expanded){
        Tree tree = new Tree();
        tree.setId(ReflectUtils.invokeGetter(item,"id")+"");
        tree.setText(ReflectUtils.invokeGetter(item,fieldName4Text));
        tree.setHref(ReflectUtils.invokeGetter(item,"href"));
        tree.setObj(item);

        State state = new State();
        state.setChecked(false);
        state.setDisabled(false);
        state.setSelected(selected);
        state.setExpanded(expanded);

        if(checkedId !=null){
            boolean isMatch = checkedId.contains(ReflectUtils.invokeGetter(item, "id"));
            state.setSelected(isMatch);
            state.setChecked(isMatch);
            tree.setState(state);
        }else{
            tree.setState(state);
        }
        tree.setNodes(getChildRole(ReflectUtils.invokeGetter(item,"id"),childList,fieldName4Text,checkedId, selected, expanded));
        treeList.add(tree);
    }

    private static List<Tree> getChildRole(Long parentid,List childList, String fieldName4Text, List checkedId, boolean selected, boolean expanded){
        List<Tree> treeList = new ArrayList();
        childList.stream().forEach(item->{
            if(!parentid.equals(ReflectUtils.invokeGetter(item,"parentid"))){
                return;
            }
            packageTreeList(treeList,item,childList,fieldName4Text,checkedId, selected,expanded);
        });
        return treeList;
    }

    static class Tree implements Serializable {
        private String id;

        private String text;

        private String icon;

        private String selectedIcon;

        private String selectable;

        private String href;

        private State state;

        private Object obj;

        public Object getObj() {
            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSelectedIcon() {
            return selectedIcon;
        }

        public void setSelectedIcon(String selectedIcon) {
            this.selectedIcon = selectedIcon;
        }

        public String getSelectable() {
            return selectable;
        }

        public void setSelectable(String selectable) {
            this.selectable = selectable;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public TreeUtils.State getState() {
            return state;
        }

        public void setState(TreeUtils.State state) {
            this.state = state;
        }

        private List<Tree> nodes;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public List<Tree> getNodes() {
            return nodes;
        }

        public void setNodes(List<Tree> nodes) {
            this.nodes = nodes;
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this);
        }
    }

    static class State{
        private boolean checked;
        private boolean disabled;
        private boolean expanded;
        private boolean selected;

        public State(){

        }

        public State(boolean checked, boolean disabled, boolean expanded, boolean selected) {
            this.checked = checked;
            this.disabled = disabled;
            this.expanded = expanded;
            this.selected = selected;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            this.expanded = expanded;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
