package com.escredit.search.config;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liyongping on 2020/6/30 2:33 PM
 */
@Configuration
@EnableConfigurationProperties(value = {SearchProperties.class})
@ConditionalOnProperty(prefix = "escredit.search", name = "enable", havingValue = "true", matchIfMissing = true)
public class SearchAutoConfig {

    @Autowired
    private SearchProperties searchProperties;

    @Bean
    public SearchSourceBuilder searchSourceBuilder(){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.timeout(new TimeValue(searchProperties.getTimeout()));
        sourceBuilder.trackTotalHits(searchProperties.getTrackTotalHits());
        sourceBuilder.from(0);
        sourceBuilder.size(20);
        return sourceBuilder;
    }

    @Bean
    public SearchRequest searchRequest(){
        String[] queryIndices = searchProperties.getQueryIndices();
        if(queryIndices == null){
            return new SearchRequest();
        }
        SearchRequest searchRequest = new SearchRequest(queryIndices);
        searchRequest.source(searchSourceBuilder());
        return searchRequest;
    }

}
