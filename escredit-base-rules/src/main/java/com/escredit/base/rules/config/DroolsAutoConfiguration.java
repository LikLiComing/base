package com.escredit.base.rules.config;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "escredit.base.rules.drools", name = "enable", havingValue = "true")
public class DroolsAutoConfiguration {

    @Bean
    public KieServices kieServices(){
        return KieServices.Factory.get();
    }

    @Bean
    @ConditionalOnMissingBean
    public KieContainer kieContainer(){
       return kieServices().getKieClasspathContainer();
    }



}
