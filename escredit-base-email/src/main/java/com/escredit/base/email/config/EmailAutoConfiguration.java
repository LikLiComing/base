package com.escredit.base.email.config;

import com.escredit.base.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author ChH
 * @create 2020/7/28
 */

@Configuration
@EnableConfigurationProperties(EmailProperties.class)
@ConditionalOnProperty(prefix = "spring.boot.mail", name = "enable", havingValue = "true")
public class EmailAutoConfiguration {

    @Resource
    private EmailProperties emailProperties;

    @Bean
    public EmailService emailService(){
        return new EmailService(emailProperties.getUsername(),
                                emailProperties.getFtlFolder(),
                                emailProperties.getFtlName());
    }

}
