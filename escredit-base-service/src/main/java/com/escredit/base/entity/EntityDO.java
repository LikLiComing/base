package com.escredit.base.entity;


import com.escredit.base.util.codec.IdHelper;

public abstract class EntityDO extends EntityBO {
    private Long id;

    private String shId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //获取短id
    public String getShId() {
        if(this.getId() != null){
            shId = IdHelper.toAlphabet(this.getId());
        }
        return shId;
    }

    public void setShId(String shId) {
        this.shId = shId;
    }
}
