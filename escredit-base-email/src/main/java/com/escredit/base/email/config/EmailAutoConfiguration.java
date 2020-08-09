package com.escredit.base.email.config;

import com.escredit.base.email.service.EmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

@Configuration
@EnableConfigurationProperties({EmailProperties.class})
@ConditionalOnClass(MailSender.class)
public class EmailAutoConfiguration {

    private EmailProperties emailProperties;

    public EmailAutoConfiguration(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Bean
    public EmailService emailService(){
        return new EmailService();
    }
}
