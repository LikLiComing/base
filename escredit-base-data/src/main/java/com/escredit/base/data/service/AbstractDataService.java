package com.escredit.base.data.service;

import com.escredit.base.data.config.DataServiceConfig;

/**
 * Created by liyongping on 2020/8/8 9:47 AM
 */
public abstract class AbstractDataService implements DataService{

    protected DataServiceConfig dataServiceConfig;

    public AbstractDataService(DataServiceConfig dataServiceConfig) {
        this.dataServiceConfig = dataServiceConfig;
    }

    protected static String generateReqTid(){
        return System.currentTimeMillis() + "";
    }


}
