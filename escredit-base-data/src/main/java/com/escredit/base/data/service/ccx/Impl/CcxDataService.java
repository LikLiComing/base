package com.escredit.base.data.service.ccx.Impl;

import com.alibaba.fastjson.JSONObject;
import com.escredit.base.data.config.DataServiceConfig;
import com.escredit.base.data.service.AbstractDataService;
import com.escredit.base.entity.DTO;
import com.escredit.base.util.codec.SignTools;
import com.escredit.base.util.collect.MapBuilder;
import com.escredit.base.web.HttpClientUtil;
import com.escredit.base.web.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by liyongping on 2020/8/8 10:23 AM
 */
@Service
public class CcxDataService extends AbstractDataService{

    private static Logger logger = LoggerFactory.getLogger(CcxDataService.class);

    public CcxDataService(DataServiceConfig dataServiceConfig) {
        super(dataServiceConfig);
    }

    @Override
    public DTO authMobile(Map requireMap, Map notRequireMap){
        String url = dataServiceConfig.getAuthMobilePath();
        //参与签名
        requireMap.put("reqId", generateReqTid());
        //生成请求url
        String reqUrl = generateUrl(requireMap,notRequireMap,url);

        DTO dto = new DTO(true);
        //执行
        excute(reqUrl,dto,"","2030");
        return dto;
    }

    @Override
    public DTO ocrId(Map requireMap, Map notRequireMap){
        String url = dataServiceConfig.getOcrIdPath();
        //参与签名
        requireMap.put("reqTid", generateReqTid());
        //不参与签名
        notRequireMap.put("idCardImage", requireMap.get("idCardImage"));
        //生成请求url
        String reqUrl = generateUrl(requireMap,notRequireMap,url);

        DTO dto = new DTO(true);
        //执行
        excute(reqUrl,dto,"");
        return dto;
    }

    @Override
    public DTO authBankCard(Map requireMap, Map notRequireMap){
        String url = dataServiceConfig.getAuthBankCardPath();
        //参与签名
        requireMap.put("reqTid", generateReqTid());
        //生成请求url
        String reqUrl = generateUrl(requireMap,notRequireMap,url);

        DTO dto = new DTO(true);
        //执行
        excute(reqUrl,dto,"");
        return dto;
    }

    /**
     * 发送短信
     * @param tel 手机号
     * @return dto.success = 是否成功，dto.object = 验证码
     */
    @Override
    public DTO sms(String tel) {
        //短信配置
        String url = dataServiceConfig.getSmsPath();
        String message = dataServiceConfig.getSmsMessage();

        // 生成随机6位码
        String authCode = "";
        while (authCode.length() < 6) {
            authCode += (int) (Math.random() * 10);
        }
        logger.info("手机号: {}, 验证码: {}", tel, authCode);
        message = String.format(message,authCode);

        //参与签名
        Map signMap = MapBuilder.newInstance().put("message", message)
                .put("mobile", tel).put("reqTid", generateReqTid()).build();
        //生成请求url
        String reqUrl = generateUrl(signMap,null,url);

        DTO dto = new DTO(true);
        //执行
        excute(reqUrl,dto,"");
        dto.setObject(authCode);
        return dto;
    }

    /**
     * 生成请求url
     * @param signMap
     * @param notSignMap
     * @param path
     * @return
     */
    private String generateUrl(Map signMap,Map notSignMap,String path){
        DataServiceConfig.Ccx ccx = dataServiceConfig.getCcx();
        String account = ccx.getAccount();
        String key = ccx.getKey();
        //追加帐号参数
        signMap.put("account",account);
        //生成签名
        String sign = SignTools.getSignature(signMap,key);

        URL url = null;
        try {
            url = new URL(ccx.getProtocal(), ccx.getHost(), ccx.getPort(),path);
        } catch (MalformedURLException e) {
        }

        String reqUrl = new UrlBuilder(url.toString()).forPath().add(signMap).put(notSignMap).put("sign", sign).build();
        return reqUrl;
    }

    /**
     * 执行并获取结果
     * @param url
     * @param dto
     * @param drillKey
     */
    protected void excute(String url, DTO dto, String drillKey){
        excute(url,dto,drillKey,"","","");
    }

    protected void excute(String url, DTO dto, String drillKey,String successCode){
        excute(url,dto,drillKey,"","",successCode);
    }

    protected void excute(String url, DTO dto, String drillKey, String resCodeKey, String resMsgKey, String successCode){
        String result = "";
        boolean isSuccess = false;
        try {
            result = HttpClientUtil.get(url);
            logger.info("url:"+url);
            logger.info("result:"+result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String resCode = jsonObject.getString(org.apache.commons.lang3.StringUtils.isNotEmpty(resCodeKey)?resCodeKey:"resCode");
            String resMsg = jsonObject.getString(org.apache.commons.lang3.StringUtils.isNotEmpty(resMsgKey)?resCodeKey:"resMsg");
            dto.setErrmsg(resMsg);
            dto.setErrcode(resCode);
            if ((org.apache.commons.lang3.StringUtils.isNotEmpty(successCode)?successCode:"0000").equals(resCode)) {
                dto.setObject(org.apache.commons.lang3.StringUtils.isNotEmpty(drillKey)?jsonObject.getJSONObject(drillKey):jsonObject);
                isSuccess = true;
            } else {
                logger.error("接口调用失败, 错误码: {} 错误信息: {}", resCode, resMsg);
                dto.putErr("105");
            }
            return;
        } catch (Exception e) {
            if ("通信失败".equals(result)) {
                logger.error("通信失败");
            } else {
                logger.error("json解析异常, json: {}", result);
            }
        }
        dto.setSuccess(isSuccess);
    }


}
