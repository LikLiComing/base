package com.escredit.search.search.service;

import com.escredit.base.entity.DTO;
import com.escredit.search.config.SearchProperties;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liyongping on 2020/6/30 12:53 PM
 */
@Component
public class SearchService {

    private Logger logger = LoggerFactory.getLogger("SearchService");

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private SearchProperties searchProperties;

    @Autowired
    private SearchRequest searchRequest;

    /**
     * 查询搜索引擎库
     * 默认范围index,field 见yml配置
     * @param dto
     * @return dto.list中对象是json格式的string
     */
    public DTO search(DTO dto) {
        prepareSearchRequest(dto.setSuccess(true));
        SearchResponse response = null;
        try {
            response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            List result = new ArrayList();
            Arrays.stream(hits).forEach(item->{
                //json格式
                result.add(item.getSourceAsString());
            });
            dto.setList(result);
        } catch (IOException e) {
            logger.error("search ERROR:{}",e.getMessage());
            dto.setSuccess(false);
        }
        logger.info("search:{}",response.toString());
        return dto;
    }

    /**
     * 初始化索引库，字段，查询条件
     * @param dto
     */
    private void prepareSearchRequest(DTO dto){
        String[] fieldNames = searchProperties.getQueryFieldNames();
        if(dto.getParam("fieldNames")!=null){
            fieldNames = dto.getParam("fieldNames");
        }
        String[] indices = searchProperties.getQueryIndices();
        if(dto.getParam("indices")!=null){
            indices = dto.getParam("indices");
        }
        searchRequest.indices(indices);

        Object text = dto.getParam("text");
        SearchSourceBuilder searchSourceBuilder = searchRequest.source();
        if(dto.getPage()!= null){
            searchSourceBuilder.from(new Long(dto.getPage().getStartIndex()).intValue());
            searchSourceBuilder.size(new Long(dto.getPage().getPageSize()).intValue());
        }
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(text,fieldNames));
    }

    public DTO searchRange(DTO dto) throws IOException {

        BoolQueryBuilder boolQuery = new BoolQueryBuilder();

        RangeQueryBuilder rangeQuery= QueryBuilders.rangeQuery("count").gte(8);
        boolQuery.filter(rangeQuery);

        MatchQueryBuilder matchQuery = new MatchQueryBuilder("aimnamec", "南京扬子开发投资有限公司");
        boolQuery.must(matchQuery);

        SearchResponse response = restHighLevelClient.search(new SearchRequest("gateway_log")
                .source(new SearchSourceBuilder()
                        .query(boolQuery)
                        .from(0)
                        .size(2)
                        .trackTotalHits(true)
                ), RequestOptions.DEFAULT);
        return dto;
    }

}
