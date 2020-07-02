package com.escredit.base.exception;


public class BusinessException extends RuntimeException{

    private String errcode;

    private String errmsg;

    public BusinessException(String errcode){
        this.errcode = errcode;
    }

    public BusinessException(String errcode, String errmsg){
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}

