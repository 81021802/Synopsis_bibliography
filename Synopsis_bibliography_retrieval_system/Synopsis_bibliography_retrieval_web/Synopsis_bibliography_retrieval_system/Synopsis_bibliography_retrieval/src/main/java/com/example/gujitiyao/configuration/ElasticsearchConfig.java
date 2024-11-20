package com.example.gujitiyao.configuration;

import com.example.gujitiyao.entity.Book;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class ElasticsearchConfig {
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @PostConstruct
    public void init(){
        elasticsearchRestTemplate.indexOps(Book.class);
    }

}
