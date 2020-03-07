package com.escredit.base.service;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 2985737702436895518L;

    public ServiceException(){
        super();
    }

    public ServiceException(String msg){
        super(msg);
    }

    public ServiceException(Exception e){
        super(e);
    }

    public ServiceException(String msg, Exception e){
        super(msg, e);
    }

    public ServiceException(Throwable cause){
        super(cause);
    }

}
