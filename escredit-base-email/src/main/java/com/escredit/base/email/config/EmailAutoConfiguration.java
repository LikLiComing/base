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

    static {
        //解决文件名超长乱码问题 xuwucheng 2020.01.15
        System.setProperty("mail.mime.splitlongparameters","false");
    }

    private EmailProperties emailProperties;

    public EmailAutoConfiguration(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Bean
    public EmailService emailService(){
        return new EmailService();
    }
}
