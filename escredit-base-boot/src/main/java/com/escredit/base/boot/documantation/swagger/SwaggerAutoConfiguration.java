package com.escredit.base.boot.documantation.swagger;

import com.escredit.base.boot.shiro.ShiroProperties;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties({SwaggerProperties.class,ShiroProperties.class})
@ConditionalOnProperty(prefix = "escredit.base.boot.swagger", name = "enable", havingValue = "true")
@EnableSwagger2
public class SwaggerAutoConfiguration {

    private SwaggerProperties swaggerProperties;

    private ShiroProperties shiroProperties;

    public SwaggerAutoConfiguration(SwaggerProperties swaggerProperties, ShiroProperties shiroProperties) {
        this.swaggerProperties = swaggerProperties;
        this.shiroProperties = shiroProperties;
    }

    private List<Parameter> setHeaderToken() {
        List<Parameter> pars = new ArrayList<>();
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name(shiroProperties.getJwt().getTokenName()).description(shiroProperties.getJwt().getTokenName())
                .modelRef(new ModelRef("string")).parameterType("header").required(false)
                .defaultValue(shiroProperties.getJwt().getTokenValue()).build();
        pars.add(tokenPar.build());
        return pars;
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerProperties.isEnable())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                .globalOperationParameters(setHeaderToken())
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .build();
    }
}
