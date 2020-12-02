package com.escredit.base.esign.exception;

/**
 * 错误码
 *
 * @author xuwucheng
 * @date 2020/8/18 15:44
 */
public class ExceptionEnum {


    public static final String CREATE_VIREFY_PERSONAL_ACCOUNT_ERROR = "创建e签宝实名认证个人账户异常";
    public static final String CREATE_VIREFY_ORGANIZE_ACCOUNT_ERROR = "创建e签宝实名认证企业账户异常";
    public static final String GET_ORGANIZE_VIREFY_URL_ERROR = "获取e签宝组织机构实名认证网页地址异常";

    public static final String CREATE_PERSONAL_ACCOUNT_ERROR = "创建e签宝个人签署账户异常";
    public static final String CREATE_PERSONAL_TEMPLATE_SEAL_ERROR = "创建e签宝个人印章异常";

    public static final String CREATE_ORGANIZE_ACCOUNT_ERROR = "创建e签宝企业签署账户异常";
    public static final String CREATE_ORGANIZE_TEMPLATE_SEAL_ERROR = "创建e签宝企业印章异常";

    public static final String CREATE_PDF_TEMPLATE_SEAL_ERROR = "创建e签宝签署PDF文件异常";

    public static final String HRT_ORGANIZE_ACCOUNT_ERROR = "数据库企业账户信息异常";

    public static final String SAVE_PERSONAL_FACEPHOTO_ERROR = "保存人脸失败照片异常";

    public static final String CREATE_ORGANIZE_LOG_FILE_ERROR = "生成企业日志文件异常";

}
