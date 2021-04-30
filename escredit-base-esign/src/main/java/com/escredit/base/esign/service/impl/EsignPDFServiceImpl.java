package com.escredit.base.esign.service.impl;

import com.escredit.base.esign.core.*;
import com.escredit.base.esign.exception.DefineException;
import com.escredit.base.esign.service.EsignPDFService;
import com.timevale.esign.sdk.tech.bean.OrganizeBean;
import com.timevale.esign.sdk.tech.bean.PersonBean;
import com.timevale.esign.sdk.tech.bean.PosBean;
import com.timevale.esign.sdk.tech.bean.SignPDFStreamBean;
import com.timevale.esign.sdk.tech.bean.result.FileDigestSignResult;
import com.timevale.esign.sdk.tech.bean.seal.OrganizeTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.PersonTemplateType;
import com.timevale.esign.sdk.tech.bean.seal.SealColor;
import com.timevale.esign.sdk.tech.impl.constants.LegalAreaType;
import com.timevale.esign.sdk.tech.impl.constants.OrganRegType;
import com.timevale.esign.sdk.tech.impl.constants.SignType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

import static com.escredit.base.esign.exception.ExceptionEnum.*;

/**
 * e签宝PDF service
 *
 * @author xuwucheng
 * @date 2020/8/18 15:35
 */
@Service
public class EsignPDFServiceImpl implements EsignPDFService {


    private static Logger logger = LoggerFactory.getLogger(EsignPDFServiceImpl.class);
    
    @Autowired(required = false)
    private AccountHelper accountHelper;

    @Autowired(required = false)
    private SealHelper sealHelper;

    @Autowired(required = false)
    private SignHelper signHelper;

    @Autowired(required = false)
    private VerifyPDFHelper verifyPDFHelper;

    @Autowired(required = false)
    private MobileCodeHelper mobileCodeHelper;

    @Autowired(required = false)
    private PdfTemplateHelper pdfTemplateHelper;


    /**
     * 创建e签宝个人签署账号
     *
     * @param name   姓名
     * @param idNo   证件号码
     * @param mobile 用于接收签署验证码的手机号码,可空
     * @return
     */
    @Override
    public String addPersonalAcct(String name, String idNo, String mobile) {
        try {

            PersonBean personBean = new PersonBean();
            // 姓名
            personBean.setName(name);
            // 证件号码
            personBean.setIdNo(idNo);
            // 用于接收签署验证码的手机号码,可空
            personBean.setMobile(mobile);

            // 个人归属地：
            // MAINLAND-大陆身份证|HONGKONG-香港居民来往内地通行证|MACAO-澳门居民来往内地通行证|TAIWAN-台湾居民来往大陆通行证
            // PASSPORT-中国护照|FOREIGN-外籍证件|OTHER-其他
            personBean.setPersonArea(LegalAreaType.MAINLAND);

            // 所属公司,可空
            personBean.setOrgan("");
            // 职位,可空
            personBean.setTitle("");

            personBean.setPersonArea(LegalAreaType.MAINLAND);

            return accountHelper.addAccount(personBean);

        } catch (Exception e) {
            logger.error(CREATE_PERSONAL_ACCOUNT_ERROR, e);
            return null;
        }
    }


    /**
     * 创建e签宝个人印章
     *
     * @return java.lang.String
     * @params [personAccountId]
     * @author xuwucheng 2020/8/18 15:52
     **/
    @Override
    public String addPersonTemplateSeal(String personAccountId) {
        try {
            // 印章模板类型,可选SQUARE-正方形印章 | RECTANGLE-矩形印章 | BORDERLESS-无框矩形印章
            PersonTemplateType personTemplateType = PersonTemplateType.RECTANGLE;

            // 印章颜色：RED-红色 | BLUE-蓝色 | BLACK-黑色
            SealColor sealColor = SealColor.RED;

            // 个人模板印章SealData
            return sealHelper.addTemplateSeal(personAccountId, personTemplateType, sealColor);

        } catch (Exception e) {
            logger.error(CREATE_PERSONAL_TEMPLATE_SEAL_ERROR, e);
            return null;
        }
    }


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
    @Override
    public String addOrganizelAcct(String organName, String organCode, String mobile, String legalName, String legalIdNo, String orgType) {
        try {

            OrganizeBean organizeBean = new OrganizeBean();
            // 企业名称
            organizeBean.setName(organName);

            // 单位类型，0-普通企业，1-社会团体，2-事业单位，3-民办非企业单位，4-党政及国家机构
            organizeBean.setOrganType(0);

            // 企业注册类型，NORMAL:组织机构代码号，MERGE：多证合一，传递社会信用代码号,REGCODE:企业工商注册码,默认NORMAL
            if ("ORGANIZATION_USC_CODE".equals(orgType)) {
                //企业
                organizeBean.setRegType(OrganRegType.MERGE);
            } else {
                //个体工商户
                if (organCode.startsWith("9")) {
                    //多证合一
                    organizeBean.setRegType(OrganRegType.MERGE);
                } else {
                    organizeBean.setRegType(OrganRegType.REGCODE);
                }
            }

            // 组织机构代码号、社会信用代码号或工商注册号
            organizeBean.setOrganCode(organCode);

            // 用于接收签署验证码的手机号码,可空
            organizeBean.setMobile(mobile);

            // 公司地址,可空
            organizeBean.setAddress("");

            // 注册类型,1-代理人注册,2-法人注册,0-缺省注册无需法人或代理人信息
            // 2-法人注册
            organizeBean.setUserType(2);

            // 法定代表姓名，当注册类型为2时必填
            organizeBean.setLegalName(legalName);

            // 法定代表人归属地,0-大陆，1-香港，2-澳门，3-台湾，4-外籍，默认0
            organizeBean.setLegalArea(0);

            // 法定代表身份证号/护照号，当注册类型为2时必填
            organizeBean.setLegalIdNo(legalIdNo);

            return accountHelper.addAccount(organizeBean);

        } catch (Exception e) {
            logger.error(CREATE_ORGANIZE_ACCOUNT_ERROR, e);
            return null;
        }
    }


    /**
     * 创建e签宝企业印章
     *
     * @param organizeAccountId
     * @param sealText          生成印章中的横向文内容 如“合同专用章、财务专用章” 可空
     * @param sealNo            生成印章中的下弦文内容 如公章防伪码（一般为13位数字）可空
     * @return
     */
    @Override
    public String addOrganizeTemplateSeal(String organizeAccountId, String sealText, String sealNo) {
        try {
            // 印章模板类型,可选STAR-标准公章 | DEDICATED-圆形无五角星章 | OVAL-椭圆形印章
            OrganizeTemplateType organizeTemplateType = OrganizeTemplateType.STAR;

            // 印章颜色：RED-红色 | BLUE-蓝色 | BLACK-黑色
            SealColor sealColor = SealColor.RED;

            // hText 生成印章中的横向文内容 如“合同专用章、财务专用章”
            String hText = sealText;

            // qText 生成印章中的下弦文内容 如公章防伪码（一般为13位数字）
            String qText = sealNo;

            // 企业模板印章SealData
            return sealHelper.addTemplateSeal(organizeAccountId, organizeTemplateType, sealColor,
                    hText, qText);

        } catch (Exception e) {
            logger.error(CREATE_ORGANIZE_TEMPLATE_SEAL_ERROR, e);
            return null;
        }
    }


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
    @Override
    public String signPersonOrOrganizePDF(String pdfPath, String accountId, String sealData, Map<String, Object> txtFields,
                                          String srcPdfPath, String targetPdfPath,
                                          String pdfKey, float posX, float posY, float widthScaling) {

        //填充PDF内容后的临时PDF路径
//        String random = RandomUtil.getRandom(5);
        String random = "";
        String filledPdfPath = pdfPath + File.separator + random + ".pdf";

        try {

            // 文档编辑密码,如果待签署PDF文件设置了编辑密码时需要填写编辑密码,否则请传入null
            String ownerPWD = null;

            //本地PDF模板文件填充
            pdfTemplateHelper.createFileFromTemplate(srcPdfPath, filledPdfPath, ownerPWD, true, txtFields);

            // 获取个人客户签署时待签署PDF文件的字节流
            byte[] srcPdfBytes = FileHelper.getFileBytes(filledPdfPath);

            // 个人客户签署盖章
            FileDigestSignResult signResult = doSignByPDFBytes(accountId, sealData, srcPdfBytes,
                    pdfKey, posX, posY, widthScaling);

            // 所有人签署完成后将PDF文件字节流保存为本地PDF文件
            byte[] signedPdfBytes = signResult.getStream();

            FileHelper.saveFileByStream(signedPdfBytes, targetPdfPath);

            //删除临时文件 filledPdfPath
//            FileUtil.deleteFile(filledPdfPath);

            return targetPdfPath;

        } catch (Exception e) {
            logger.error(CREATE_PDF_TEMPLATE_SEAL_ERROR, e);
            return null;
        }

    }


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
     *                          <p>
     *                          参数可以封装一层Map  TODO
     *                          暂时controller用同步锁控制并发
     */
    @Override
    public String signPersonAndOrganizePDF(String pdfPath, String personAccountId, String personSealData, String organizeAccountId, String organizeSealData,
                                           Map<String, Object> txtFields, String srcPdfPath, String targetPdfPath,
                                           String personPdfKey, float personPosX, float personPosY, float personWidthScaling,
                                           String organizePdfKey, float organizePosX, float organizePosY, float organizeWidthScaling) {

        //填充PDF内容后的临时PDF路径
//        String random = RandomUtil.getRandom(5);
        String random = "";
        String filledPdfPath = pdfPath + File.separator + random + ".pdf";

        try {

            // 文档编辑密码,如果待签署PDF文件设置了编辑密码时需要填写编辑密码,否则请传入null
            String ownerPWD = null;

            //本地PDF模板文件填充
            pdfTemplateHelper.createFileFromTemplate(srcPdfPath, filledPdfPath, ownerPWD, true, txtFields);

            // 获取个人客户签署时待签署PDF文件的字节流
            byte[] srcPdfBytes = FileHelper.getFileBytes(filledPdfPath);

            // 个人客户签署盖章
            FileDigestSignResult personSignResult = doSignByPDFBytes(personAccountId, personSealData, srcPdfBytes,
                    personPdfKey, personPosX, personPosY, personWidthScaling);

            // 企业客户签署时的待签署PDF文件字节流,即个人客户签署盖章成功后的PDF文件字节流
            byte[] organizeSrcPdfBytes = personSignResult.getStream();

            // 企业客户签署盖章
            FileDigestSignResult organizeSignResult = doSignByPDFBytes(organizeAccountId, organizeSealData, organizeSrcPdfBytes,
                    organizePdfKey, organizePosX, organizePosY, organizeWidthScaling);

            // 所有人签署完成后将PDF文件字节流保存为本地PDF文件
            byte[] AllSignedPdfBytes = organizeSignResult.getStream();

            FileHelper.saveFileByStream(AllSignedPdfBytes, targetPdfPath);

            //删除临时文件 filledPdfPath
//            FileUtil.deleteFile(filledPdfPath);

            return targetPdfPath;

        } catch (Exception e) {
            logger.error(CREATE_PDF_TEMPLATE_SEAL_ERROR, e);
            return null;
        }

    }


    /**
     * 签署盖章
     *
     * @param accountId    账号
     * @param sealData     签章
     * @param pdfBytes     文件流
     * @param pdfKey       根据关键字盖章
     * @param posX         签署位置X坐标,默认值为0,以pdf页面的左下角作为原点,控制距离页面左端的横向移动距离,单位为px
     * @param posY         签署位置Y坐标,默认值为0,以pdf页面的左下角作为原点,控制距离页面底端的纵向移动距离,单位为px
     * @param widthScaling 印章图片在PDF文件中的等比缩放大小,公章标准大小为4.2厘米即159px
     * @return
     * @throws DefineException
     */
    private FileDigestSignResult doSignByPDFBytes(String accountId, String sealData, byte[] pdfBytes,
                                                  String pdfKey, float posX, float posY, float widthScaling) throws DefineException {

        // signLogFileName 文档名称,此文档名称用于在e签宝服务端记录签署日志时用,非签署后PDF文件中的文件名.若为空则取待签署PDF文件中的文件名称
        String signLogFileName = "";

        // 文档编辑密码,如果待签署PDF文件设置了编辑密码时需要填写编辑密码,否则请传入null
        String ownerPWD = null;

        SignPDFStreamBean signPDFStreamBean = doSetSignPDFStreamBean(pdfBytes, null, signLogFileName, ownerPWD);

        // 签章类型,Single-单页签章、Multi-多页签章、Edges-骑缝章、Key-关键字签章
        SignType signType = SignType.Key;

        // 印章SealData
        String personSealData = sealData;

        // 设置个人客户签章位置信息
        PosBean posBean = setPosBean(signType, pdfKey, null, posX, posY, widthScaling);

        // 个人客户签署盖章
        FileDigestSignResult fileDigestSignResult = signHelper
                .localSignPDF(accountId, personSealData, signPDFStreamBean, posBean, signType);

        return fileDigestSignResult;
    }


    /**
     * 设置签署PDF文档信息（文件流方式）
     *
     * @param pdfBytes        待签署PDF文件字节流
     * @param outPdfPath      签署后PDF文件本地保存路径,如果希望签署后依然返回PDF文件字节流时请设置该属性为空
     * @param signLogFileName 文档名称,此文档名称用于在e签宝服务端记录签署日志时用,非签署后PDF文件中
     *                        的文件名.若为空则取待签署PDF文件中的文件名称
     * @param ownerPWD        文档编辑密码,如果待签署PDF文件设置了编辑密码时需要填写编辑密码,否则请传入null
     * @return
     */
    private static SignPDFStreamBean doSetSignPDFStreamBean(byte[] pdfBytes, String outPdfPath, String signLogFileName,
                                                            String ownerPWD) {

        SignPDFStreamBean signPDFStreamBean = new SignPDFStreamBean();

        signPDFStreamBean.setStream(pdfBytes);
        signPDFStreamBean.setDstPdfFile(outPdfPath);
        signPDFStreamBean.setFileName(signLogFileName);
        signPDFStreamBean.setOwnerPassword(ownerPWD);

        return signPDFStreamBean;
    }


    /**
     * 设置签章位置信息
     *
     * @param signType 签署类型
     * @param key      关键字
     * @param page     签署页码，单页签署为"1"，多页签署为类似“1-3,5，8"
     * @param posX     签署位置的X坐标，默认值0，以pdf页码的左下角为原点，
     *                 控制距离页码左端的横向移动距离，单位为px
     * @param posY     签署位置的Y坐标，默认值0，以pdf页码的左下角为原点，
     *                 控制距离页码左端的横向移动距离，单位为px
     * @param width    印章图片在pdf文件中的等比缩放大小，公章标准大小为4.2cm，即159px
     * @return
     */
    private static PosBean setPosBean(SignType signType, String key, String page, float posX, float posY, float width) {

        PosBean posBean = new PosBean();

        posBean.setPosType(SignType.Key == signType ? 1 : 0);
        posBean.setPosPage(page);
        posBean.setKey(key);
        posBean.setPosX(posX);
        posBean.setPosY(posY);
        posBean.setWidth(width);

        return posBean;
    }



    @Override
    public byte[] signPdfByBytes(String accountId, String sealData, SignType signType, String keyword, String page, float posX, float posY, byte[] pdfBytes) {


        try {

            // 签署后PDF文件本地保存路径,如果希望签署后依然返回PDF文件字节流时请设置该属性为空
            SignPDFStreamBean signPDFStreamBean = doSetSignPDFStreamBean(pdfBytes, null, "", null);

            // 印章图片在PDF文件中的等比缩放大小,公章标准大小为4.2厘米即159px
            float widthScaling = 159F;

            // 设置企业客户签章位置信息
            PosBean posBean = setPosBean(signType, keyword, page, posX, posY, widthScaling);

            // 企业客户签署盖章
            FileDigestSignResult fileDigestSignResult = signHelper.localSignPDF(accountId, sealData, signPDFStreamBean, posBean, signType);

            // 所有人签署完成后将PDF文件字节流保存为本地PDF文件
            return fileDigestSignResult.getStream();

        } catch (Exception e) {
            logger.error("pdf签章失败, accountid: {}, stacktrace: {}", accountId, e.toString());
            return null;
        }

    }


    @Override
    public byte[] signPdfByBytesV2(String accountId, String sealId, SignType signType, String keyword, String page, float posX, float posY, byte[] pdfBytes) {


        try {

            // 签署后PDF文件本地保存路径,如果希望签署后依然返回PDF文件字节流时请设置该属性为空
            SignPDFStreamBean signPDFStreamBean = doSetSignPDFStreamBean(pdfBytes, null, "", null);

            // 印章图片在PDF文件中的等比缩放大小,公章标准大小为4.2厘米即159px
            float widthScaling = 159F;

            // 设置企业客户签章位置信息
            PosBean posBean = setPosBean(signType, keyword, page, posX, posY, widthScaling);

            // 企业客户签署盖章
            FileDigestSignResult fileDigestSignResult = signHelper.localSignPDFV2(signPDFStreamBean, posBean, sealId, signType);

            // 所有人签署完成后将PDF文件字节流保存为本地PDF文件
            return fileDigestSignResult.getStream();

        } catch (Exception e) {
            logger.error("pdf签章失败, accountid: {}, stacktrace: {}", accountId, e.toString());
            return null;
        }

    }

    @Override
    public String signPdfByFile(String accountId, String sealData, SignType signType, String keyword, String page, float posX, float posY, String srcPdfPath, String targetPdfPath) {

        try {

            byte[] srcPdfBytes = FileHelper.getFileBytes(srcPdfPath);

            SignPDFStreamBean signPDFStreamBean = doSetSignPDFStreamBean(srcPdfBytes, null, "", null);

            // 印章图片在PDF文件中的等比缩放大小,公章标准大小为4.2厘米即159px
            float widthScaling = 159F;

            // 设置企业客户签章位置信息
            PosBean posBean = setPosBean(signType, keyword, page, posX, posY, widthScaling);

            // 企业客户签署盖章
            FileDigestSignResult fileDigestSignResult = signHelper.localSignPDF(accountId, sealData, signPDFStreamBean, posBean, signType);
            byte[] signedPdfBytes = fileDigestSignResult.getStream();

            FileHelper.saveFileByStream(signedPdfBytes, targetPdfPath);

            return targetPdfPath;

        } catch (Exception e) {
            logger.error(CREATE_PDF_TEMPLATE_SEAL_ERROR, e);
            return null;
        }

    }

    @Override
    public String signPdfByFileV2(String accountId, String sealId, SignType signType, String keyword, String page, float posX, float posY, String srcPdfPath, String targetPdfPath) {

        try {

            byte[] srcPdfBytes = FileHelper.getFileBytes(srcPdfPath);
            // 签署后PDF文件本地保存路径,如果希望签署后依然返回PDF文件字节流时请设置该属性为空
            SignPDFStreamBean signPDFStreamBean = doSetSignPDFStreamBean(srcPdfBytes, null, "", null);

            // 印章图片在PDF文件中的等比缩放大小,公章标准大小为4.2厘米即159px
            float widthScaling = 159F;

            // 设置企业客户签章位置信息
            PosBean posBean = setPosBean(signType, keyword, page, posX, posY, widthScaling);

            // 企业客户签署盖章
            FileDigestSignResult fileDigestSignResult = signHelper.localSignPDFV2(signPDFStreamBean, posBean, sealId, signType);
            byte[] signedPdfBytes = fileDigestSignResult.getStream();

            FileHelper.saveFileByStream(signedPdfBytes, targetPdfPath);

            return targetPdfPath;

        } catch (Exception e) {
            logger.error(CREATE_PDF_TEMPLATE_SEAL_ERROR, e);
            return null;
        }
    }

}
