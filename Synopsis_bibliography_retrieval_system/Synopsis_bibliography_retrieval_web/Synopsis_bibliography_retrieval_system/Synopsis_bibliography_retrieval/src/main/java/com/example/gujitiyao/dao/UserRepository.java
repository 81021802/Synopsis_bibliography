package com.example.gujitiyao.dao;

import com.example.gujitiyao.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface UserRepository extends ElasticsearchRepository<Book, Long> {
    Book findByAuthor(String author);
}
