package com.escredit.base.rules.config;

import com.escredit.base.rules.service.KieSessionService;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DroolsProperties.class)
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

    @Bean
    public KieSessionService kieSessionService(DroolsProperties droolsProperties,KieContainer kieContainer){
        KieSessionService kieSessionService = new KieSessionService();
        kieSessionService.setDroolsProperties(droolsProperties);
        kieSessionService.setKieContainer(kieContainer);
        return kieSessionService;
    }

}
