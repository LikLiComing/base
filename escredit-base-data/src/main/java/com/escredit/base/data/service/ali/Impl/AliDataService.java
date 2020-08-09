package com.escredit.base.data.service.ali.Impl;

import com.escredit.base.data.config.DataServiceConfig;
import com.escredit.base.data.service.AbstractDataService;
import com.escredit.base.data.service.DataService;
import com.escredit.base.entity.DTO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by liyongping on 2020/8/8 11:29 AM
 */
@Service
public class AliDataService extends AbstractDataService {

    public AliDataService(DataServiceConfig dataServiceConfig) {
        super(dataServiceConfig);
    }

    @Override
    public DTO sms(String tel) {
        return null;
    }

    @Override
    public DTO authMobile(Map requireMap, Map notRequireMap) {
        return null;
    }

    @Override
    public DTO ocrId(Map requireMap, Map notRequireMap) {
        return null;
    }

    @Override
    public DTO authBankCard(Map requireMap, Map notRequireMap) {
        return null;
    }
}
