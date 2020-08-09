package com.escredit.base.boot.web;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebMvc
 */
@ConfigurationProperties(prefix = "escredit.base.boot.web.mvc")
public class WebMvcProperties {

    private boolean enable = false;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MultipartResolver multipartResolver = new MultipartResolver();

    public static class ObjectMapper{

        private String dateFormat = "yyyy-MM-dd HH:mm:ss";

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }

    }

    public static class MultipartResolver{

        private String defaultEncoding = "UTF-8";

        /**
         * 启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
         */
        private boolean resolveLazily = true;

        private Integer maxInMemorySize = 40960;

        /**
         * 上传文件大小500M
         */
        private Long maxUploadSize = 500*1024*1024L;

        public String getDefaultEncoding() {
            return defaultEncoding;
        }

        public void setDefaultEncoding(String defaultEncoding) {
            this.defaultEncoding = defaultEncoding;
        }

        public boolean isResolveLazily() {
            return resolveLazily;
        }

        public void setResolveLazily(boolean resolveLazily) {
            this.resolveLazily = resolveLazily;
        }

        public Integer getMaxInMemorySize() {
            return maxInMemorySize;
        }

        public void setMaxInMemorySize(Integer maxInMemorySize) {
            this.maxInMemorySize = maxInMemorySize;
        }

        public Long getMaxUploadSize() {
            return maxUploadSize;
        }

        public void setMaxUploadSize(Long maxUploadSize) {
            this.maxUploadSize = maxUploadSize;
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public MultipartResolver getMultipartResolver() {
        return multipartResolver;
    }

    public void setMultipartResolver(MultipartResolver multipartResolver) {
        this.multipartResolver = multipartResolver;
    }
}
