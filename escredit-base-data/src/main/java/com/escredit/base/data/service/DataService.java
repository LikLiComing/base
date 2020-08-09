package com.escredit.base.data.service;

import com.escredit.base.data.config.DataServiceConfig;
import com.escredit.base.data.service.ccx.Impl.CcxDataService;
import com.escredit.base.entity.DTO;

import java.util.Map;

/**
 * Created by liyongping on 2020/8/8 10:23 AM
 */
public interface DataService {

    /**
     * 发送短信
     * @param tel 手机号
     * @return dto.success = 是否成功，dto.object = 验证码
     */
    DTO sms(String tel) ;

    /**
     * 运营商三要素核验
     * CCX服务参数说明
     * @param requireMap name 姓名;cid 证件号;mobile 手机号
     * @param notRequireMap cidType 证件类型;mobileType 运营商;encryptType 加密方式;encrypItem 加密项
     * @return
     */
    DTO authMobile(Map requireMap, Map notRequireMap);

    /**
     * OCR身份证信息识别
     * CCX服务参数说明
     * @param requireMap idCardImage 身份证base64
     * @param notRequireMap 没有，但不能为null
     * @return
     */
    DTO ocrId(Map requireMap, Map notRequireMap);

    /**
     * 账户欺诈四维风险识别
     * CCX服务参数说明
     * @param requireMap name 姓名；cid 身份证；card 银行卡；mobile 手机号
     * @param notRequireMap 没有，但不能为null
     * @return
     */
    DTO authBankCard(Map requireMap, Map notRequireMap);

    class Factory {

        public static DataService get(DataServiceConfig dataServiceConfig) {
            return new CcxDataService(dataServiceConfig);
        }

        public static DataService get(ProviderEnum providerEnum,DataServiceConfig dataServiceConfig) {
            switch (providerEnum) {
                case ALI:
                case TENCENT:
                case BAIDU:
                default:
                    return new CcxDataService(dataServiceConfig);
            }
        }
    }

    enum ProviderEnum {

        CCX,

        TENCENT,

        ALI,

        BAIDU;
    }

}
