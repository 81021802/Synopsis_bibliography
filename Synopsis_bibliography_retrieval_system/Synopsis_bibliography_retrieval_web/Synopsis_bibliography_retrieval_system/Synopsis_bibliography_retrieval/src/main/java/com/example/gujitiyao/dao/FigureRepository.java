package com.example.gujitiyao.dao;

import com.example.gujitiyao.entity.Figure;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FigureRepository extends ElasticsearchRepository<Figure,Long> {

}
