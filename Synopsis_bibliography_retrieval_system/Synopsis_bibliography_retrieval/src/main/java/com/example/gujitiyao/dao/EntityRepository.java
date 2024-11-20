package com.example.gujitiyao.dao;

import com.example.gujitiyao.entity.Entity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends ElasticsearchRepository<Entity,Long> {

}
