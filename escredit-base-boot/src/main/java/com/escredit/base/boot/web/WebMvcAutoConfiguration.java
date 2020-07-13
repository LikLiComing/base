package com.escredit.base.boot.web;

import com.escredit.base.boot.documantation.swagger.SwaggerProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(WebMvcProperties.class)
@ConditionalOnProperty(prefix = "escredit.base.boot.web.mvc", name = "enable", havingValue = "true")
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    @Autowired
    private WebMvcProperties webMvcProperties;

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")   //支持跨域的访问路径
                .allowedOrigins("*")   //允许请求的域名访问
                .allowedHeaders("*")
                .allowedMethods("*") ;  //支持跨域的方法

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(swaggerProperties.isEnable()){
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }

    /**
     * 去除输出null, ""
     * 输出日期格式yyyy-MM-dd HH:mm:ss
     * json中有新增的字段并且是实体类类中不存在的，不报错
     * @param builder
     * @return
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.getSerializerProvider()
                .setNullValueSerializer(new JsonSerializer<Object>() {
                    @Override
                    public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
                        arg1.writeString("");
                    }
                });
        objectMapper.setDateFormat(new SimpleDateFormat(webMvcProperties.getObjectMapper().getDateFormat()));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        WebMvcProperties.MultipartResolver multipartResolver = webMvcProperties.getMultipartResolver();
        resolver.setDefaultEncoding(multipartResolver.getDefaultEncoding());
        resolver.setResolveLazily(multipartResolver.isResolveLazily());
        resolver.setMaxInMemorySize(multipartResolver.getMaxInMemorySize());
        resolver.setMaxUploadSize(multipartResolver.getMaxUploadSize());
        return resolver;
    }
}
