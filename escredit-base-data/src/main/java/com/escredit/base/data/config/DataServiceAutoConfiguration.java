package com.escredit.base.data.config;

import com.escredit.base.data.service.DataService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DataServiceConfig.class)
public class DataServiceAutoConfiguration {

    private DataServiceConfig dataServiceConfig;

    public DataServiceAutoConfiguration(DataServiceConfig dataServiceConfig) {
        this.dataServiceConfig = dataServiceConfig;
    }

    /**
     * 注册指定的服务提供者
     * @return
     */
    @Bean
    public DataService dataService(){
        String provider = dataServiceConfig.getProvider();
        DataService dataService = DataService.Factory.get(DataService.ProviderEnum.valueOf(provider),dataServiceConfig);
        return dataService;
    }
}
