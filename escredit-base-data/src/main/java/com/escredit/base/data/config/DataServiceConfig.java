package com.escredit.base.data.config;

import com.escredit.base.data.service.DataService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "escredit.data.service")
public class DataServiceConfig {

    /**
     * 服务提供方，默认为CCX，见DataService.ProviderEnum
     * 调用者也可通过DataService.Factory方法指定
     * 见包路径ccx,ali
     */
    private String provider = DataService.ProviderEnum.CCX.name();

    private Ccx ccx = new Ccx();

    /**
     * 数据平台配置
     */
    public static class Ccx{

        private String protocal = "http";

        private String host = "api.ccxcredit.com";

        private Integer port = 80;

        /**
         * 服务帐号
         */
        private String account;

        /**
         * 服务密钥
         */
        private String key;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getProtocal() {
            return protocal;
        }

        public void setProtocal(String protocal) {
            this.protocal = protocal;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }

    /**
     * 短信url
     */
    private String smsPath = "/data-service/sms/general";

    /**
     * 短信格式
     */
    private String smsMessage = "您的验证码为：%s,有效时间2分钟[中诚信征信]";

    /**
     * ocr身份证Url
     */
    private String ocrIdPath = "/data-service/identity/ocridcard";

    /**
     * 账户欺诈四维风险识别
     */
    private String authBankCardPath = "/data-service/auth/cncm/t5";

    /**
     * 运营商三要素核验
     */
    private String authMobilePath = "/data-service/telecom/idencheck/type";

    private Ali ali = new Ali();

    private Tencent tencent = new Tencent();

    /**
     * 阿里
     */
    public static class Ali{

    }

    /**
     * 腾讯
     */
    public static class Tencent{

    }

    public String getSmsPath() {
        return smsPath;
    }

    public void setSmsPath(String smsPath) {
        this.smsPath = cleanContextPath(smsPath);
    }

    public String getOcrIdPath() {
        return ocrIdPath;
    }

    public void setOcrIdPath(String ocrIdPath) {
        this.ocrIdPath = ocrIdPath;
    }

    public String getAuthBankCardPath() {
        return authBankCardPath;
    }

    public void setAuthBankCardPath(String authBankCardPath) {
        this.authBankCardPath = authBankCardPath;
    }

    public String getAuthMobilePath() {
        return authMobilePath;
    }

    public void setAuthMobilePath(String authMobilePath) {
        this.authMobilePath = authMobilePath;
    }

    private String cleanContextPath(String contextPath) {
        String candidate = StringUtils.trimWhitespace(contextPath);
        if (StringUtils.hasText(candidate) && candidate.endsWith("/")) {
            return candidate.substring(0, candidate.length() - 1);
        }
        return candidate;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Ali getAli() {
        return ali;
    }

    public void setAli(Ali ali) {
        this.ali = ali;
    }

    public Tencent getTencent() {
        return tencent;
    }

    public void setTencent(Tencent tencent) {
        this.tencent = tencent;
    }

    public String getSmsMessage() {
        return smsMessage;
    }

    public void setSmsMessage(String smsMessage) {
        this.smsMessage = smsMessage;
    }

    public Ccx getCcx() {
        return ccx;
    }

    public void setCcx(Ccx ccx) {
        this.ccx = ccx;
    }

}
