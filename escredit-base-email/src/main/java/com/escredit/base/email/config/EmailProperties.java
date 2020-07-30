package com.escredit.base.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ChH
 * @create 2020/7/28
 */
@Component
@ConfigurationProperties(prefix = "escredit.base.mail")
public class EmailProperties {

    private Common common = new Common();

    private Ftl ftl = new Ftl();

    /**
     * 常规设置
     */
    public static class Common{
        /**
         * 收件人
         */
        private String to;

        /**
         * 收件人称呼
         */
        private String hello;

        /**
         * 主题
         */
        private String subject;

        /**
         * 正文
         */
        private String content;

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getHello() {
            return hello;
        }

        public void setHello(String hello) {
            this.hello = hello;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * ftl设置
     */
    public static class Ftl{

        /**
         * 模版开关
         */
        private boolean enable;

        /**
         * ftl模板目录
         */
        private String ftlFolder = "email";

        /**
         * ftl模板名称
         */
        private String ftlName = "default.ftl";

        private String ftlBasePackagePath = "/templates";

        /**
         * 变量
         */
        private Map<String, Object> dataModel;

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public Map<String, Object> getDataModel() {
            return dataModel;
        }

        public void setDataModel(Map<String, Object> dataModel) {
            this.dataModel = dataModel;
        }

        public String getFtlFolder() {
            return ftlFolder;
        }

        public void setFtlFolder(String ftlFolder) {
            this.ftlFolder = ftlFolder;
        }

        public String getFtlName() {
            return ftlName;
        }

        public void setFtlName(String ftlName) {
            this.ftlName = ftlName;
        }

        public String getFtlBasePackagePath() {
            return ftlBasePackagePath;
        }

        public void setFtlBasePackagePath(String ftlBasePackagePath) {
            this.ftlBasePackagePath = ftlBasePackagePath;
        }
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public Ftl getFtl() {
        return ftl;
    }

    public void setFtl(Ftl ftl) {
        this.ftl = ftl;
    }
}
