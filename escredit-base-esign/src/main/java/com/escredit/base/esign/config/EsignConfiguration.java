package com.escredit.base.esign.config;

import com.escredit.base.esign.service.EsignPDFService;
import com.escredit.base.esign.service.impl.EsignPDFServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsignConfiguration {


    @Bean
    public EsignPDFService esignService(){
        return new EsignPDFServiceImpl();
    }

}
