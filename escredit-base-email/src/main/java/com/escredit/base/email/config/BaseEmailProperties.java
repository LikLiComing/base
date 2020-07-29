package com.escredit.base.email.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ChH
 * @create 2020/7/28
 */
@ConfigurationProperties(prefix = "escredit.base.mail")
public class BaseEmailProperties {

    /**
     * 默认值
     */
    private static final String defaultFtlFolder;
    private static final String defaultFtlName;
    private static final Charset DEFAULT_CHARSET;

    static {
        DEFAULT_CHARSET = StandardCharsets.UTF_8;
        defaultFtlFolder = "email";
        defaultFtlName = "default.ftl";
    }

    public BaseEmailProperties() {
        this.defaultEncoding = DEFAULT_CHARSET;
        this.ftlFolder = defaultFtlFolder;
        this.ftlName = defaultFtlName;
        this.properties = new HashMap();
    }

    private boolean enable;
    /**
     * ftl模板目录
     */
    private String ftlFolder;

    /**
     * ftl模板名称
     */
    private String ftlName;

    /**
     * 邮箱服务器地址
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱协议，默认"smtp"
     */
    private String protocol = "smtp";

    /**
     * 默认编码
     */
    private Charset defaultEncoding;

    /**
     * 配置Map
     */
    private Map<String, String> properties;

    /**
     * JNDI名称
     */
    private String jndiName;

    /**
     * 测试连接
     */
    private boolean testConnection;

    /**
     * 邮件发送者，默认配置邮箱的用户,见getSender()
     */
    private String sender;

    /**
     * 邮件接收者，默认配置邮箱的用户,见getReceiver()
     */
    private String receiver;

    /**
     * 发送邮件主题
     */
    private String sendMailTitle="异常信息";

    /**
     * 是否打印日志
     */
    private boolean isPrintLog=false;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Charset getDefaultEncoding() {
        return this.defaultEncoding;
    }

    public void setDefaultEncoding(Charset defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public Map<String, String> getProperties() {
        return this.properties;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public String getJndiName() {
        return this.jndiName;
    }

    public boolean isTestConnection() {
        return this.testConnection;
    }

    public void setTestConnection(boolean testConnection) {
        this.testConnection = testConnection;
    }

    public String getSender() {
        if(null==sender||"".equals(sender)){
            return getUsername();
        }
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        if(null==receiver||"".equals(receiver)){
            return getUsername();
        }
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendMailTitle() {
        return sendMailTitle;
    }

    public void setSendMailTitle(String sendMailTitle) {
        this.sendMailTitle = sendMailTitle;
    }

    public boolean isPrintLog() {
        return this.isPrintLog;
    }

    public void setPrintLog(boolean printLog) {
        isPrintLog = printLog;
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
