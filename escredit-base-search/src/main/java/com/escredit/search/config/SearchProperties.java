package com.escredit.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by liyongping on 2020/6/30 2:40 PM
 */
@ConfigurationProperties(prefix = "escredit.search")
public class SearchProperties {

    private Long timeout = 1000L;

    private Boolean trackTotalHits = Boolean.TRUE;

    private String[] queryFieldNames;

    private String[] queryIndices;

    private Boolean enable = Boolean.TRUE;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public Boolean getTrackTotalHits() {
        return trackTotalHits;
    }

    public void setTrackTotalHits(Boolean trackTotalHits) {
        this.trackTotalHits = trackTotalHits;
    }

    public String[] getQueryFieldNames() {
        return queryFieldNames;
    }

    public void setQueryFieldNames(String[] queryFieldNames) {
        this.queryFieldNames = queryFieldNames;
    }

    public String[] getQueryIndices() {
        return queryIndices;
    }

    public void setQueryIndices(String[] queryIndices) {
        this.queryIndices = queryIndices;
    }
}
