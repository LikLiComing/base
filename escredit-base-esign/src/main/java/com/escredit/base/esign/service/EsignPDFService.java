package com.escredit.base.esign.service;

import com.timevale.esign.sdk.tech.impl.constants.SignType;

import java.util.Map;

/**
 * e签宝PDF service
 *
 * @author xuwucheng
 * @date 2020/8/18 15:35
 */
public interface EsignPDFService {



    /**
     * 创建e签宝个人签署账号
     *
     * @param name   姓名
     * @param idNo   证件号码
     * @param mobile 用于接收签署验证码的手机号码,可空
     * @return
     */
    public String addPersonalAcct(String name, String idNo, String mobile);


    /**
     * 创建e签宝个人印章
     *
     * @return java.lang.String
     * @params [personAccountId]
     * @author xuwucheng 2020/8/18 15:52
     **/
    public String addPersonTemplateSeal(String personAccountId);


    /**
     * 创建e签宝企业签署账号
     *
     * @param organName 企业名称
     * @param organCode 组织机构代码号、社会信用代码号或工商注册号
     * @param mobile    用于接收签署验证码的手机号码,可空
     * @param legalName 法定代表姓名
     * @param legalIdNo 法定代表身份证号/护照号
     * @return
     */
    public String addOrganizelAcct(String organName, String organCode, String mobile, String legalName, String legalIdNo, String orgType);


    /**
     * 创建e签宝企业印章
     *
     * @param organizeAccountId
     * @param sealText          生成印章中的横向文内容 如“合同专用章、财务专用章” 可空
     * @param sealNo            生成印章中的下弦文内容 如公章防伪码（一般为13位数字）可空
     * @return
     */
    public String addOrganizeTemplateSeal(String organizeAccountId, String sealText, String sealNo);


    /**
     * 文件流方式企业或个人单个签章PDF
     *
     * @param accountId
     * @param sealData
     * @param txtFields     模板中包含待填充文本域时，文本域Key-Value组合
     * @param srcPdfPath    待签署PDF文件路径
     * @param targetPdfPath 签署PDF文件路径
     * @param pdfKey
     * @param posX
     * @param posY
     * @param widthScaling
     * @return
     */
    public String signPersonOrOrganizePDF(String pdfPath, String accountId, String sealData, Map<String, Object> txtFields,
                                          String srcPdfPath, String targetPdfPath,
                                          String pdfKey, float posX, float posY, float widthScaling);


    /**
     * 文件流方式同时生成个人和企业签章PDF
     *
     * @param personAccountId
     * @param personSealData
     * @param organizeAccountId
     * @param organizeSealData
     * @param txtFields         模板中包含待填充文本域时，文本域Key-Value组合
     * @param srcPdfPath        待签署PDF文件路径
     * @param targetPdfPath     签署PDF文件路径
     */
    public String signPersonAndOrganizePDF(String pdfPath, String personAccountId, String personSealData, String organizeAccountId, String organizeSealData,
                                           Map<String, Object> txtFields, String srcPdfPath, String targetPdfPath,
                                           String personPdfKey, float personPosX, float personPosY, float personWidthScaling,
                                           String organizePdfKey, float organizePosX, float organizePosY, float organizeWidthScaling);



    public byte[] signPdfByBytes(String accountId, String sealData, SignType signType, String keyword, String page, float posX, float posY,
                                 byte[] pdfBytes);

    public byte[] signPdfByBytesV2(String accountId, String sealId, SignType signType, String keyword, String page, float posX, float posY,
                                 byte[] pdfBytes);

    public String signPdfByFile(String accountId, String sealData, SignType signType, String keyword, String page, float posX, float posY,
                                String srcPdfPath, String targetPdfPath);

    public String signPdfByFileV2(String accountId, String sealId, SignType signType, String keyword, String page, float posX, float posY,
                                String srcPdfPath, String targetPdfPath);

}
