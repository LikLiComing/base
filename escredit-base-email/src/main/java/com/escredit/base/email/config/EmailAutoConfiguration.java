package com.escredit.base.email.config;

import com.escredit.base.email.service.EmailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author ChH
 * @create 2020/7/28
 */

@Configuration
@EnableConfigurationProperties({BaseEmailProperties.class})
@ConditionalOnProperty(prefix = "escredit.base.mail", name = "enable", havingValue = "true")
public class EmailAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private BaseEmailProperties mailProperties;

    @Bean
    @Primary
    @DependsOn(value = "mailSender")
    public EmailService emailService(){
        return new EmailService(mailProperties);
    }

    @Bean
    public MailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        Properties properties = new Properties();
        properties.putAll(mailProperties.getProperties());
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

}
