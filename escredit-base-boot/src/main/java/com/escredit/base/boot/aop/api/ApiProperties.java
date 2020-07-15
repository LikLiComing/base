package com.escredit.base.boot.aop.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "escredit.base.boot.api")
public class ApiProperties {

    private Idempotent idempotent = new Idempotent();

    public Idempotent getIdempotent() {
        return idempotent;
    }

    public void setIdempotent(Idempotent idempotent) {
        this.idempotent = idempotent;
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

}
