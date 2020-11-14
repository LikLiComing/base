package com.escredit.base.boot.aop.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "escredit.base.boot.api")
public class ApiProperties {

    private Idempotent idempotent = new Idempotent();

    private Limit limit = new Limit();

    public Idempotent getIdempotent() {
        return idempotent;
    }

    public void setIdempotent(Idempotent idempotent) {
        this.idempotent = idempotent;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    /**
     * 幂等性
     */
    public class Idempotent{

        /**
         * 校验token的名称
         */
        private String tokenName = "authhead";

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }
    }

    /**
     * 限流
     */
    public class Limit{
        /**s
         * 请求频率上限
         */
        private Integer maxRequest = 10;

        /**
         * 请求有效期(秒)
         */
        private Integer timeout = 10;

        public Integer getMaxRequest() {
            return maxRequest;
        }

        public void setMaxRequest(Integer maxRequest) {
            this.maxRequest = maxRequest;
        }

        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }
    }



}
