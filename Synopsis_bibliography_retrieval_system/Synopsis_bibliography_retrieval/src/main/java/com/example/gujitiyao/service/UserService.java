package com.example.gujitiyao.service;

import com.example.gujitiyao.entity.Book;
import com.example.gujitiyao.entity.Entity;
import com.example.gujitiyao.entity.Figure;
import com.example.gujitiyao.utils.Result;
import org.elasticsearch.index.query.Operator;

import java.util.List;
import java.util.Map;


public interface UserService {
    Book save(Book book);
    void saveAll(List<Book> bookList);
    Book getById(Long id);
    List<Entity> getByTextID(Long textID);
    void deleteAll();
    void deleteById(Long id);
    List<Book> getAll();
    List<Map.Entry<String, Long>> groupByBookName();
    List<Map.Entry<String, Long>> groupByEditionDynasty();
    List<Map.Entry<String, Long>> groupByFigureDynasty();
    List<Map.Entry<String, Long>> findByBookName(String bookName);
    List<Map.Entry<String, Long>> findByBookNamePart(String bookName,String part);
    List<Figure> getTop50FiguresByCount();
    <T> List<T> queryStringQuery(Map<String,Float> fields, String queryString, String analyzer, Operator operator,Class<T> classType);
    <T> Result boolQueryBuildByMust(Map<String,String> fields,Integer pageNum,Integer pageSize,String preTags,String postTags, Class<T> classType);
    <T> List<T> boolQueryBuildByMustNot(Map<String,String[]> fields, Class<T> classType);
    <T> Result boolQueryBuildByShould(Map<String,String> fields,Integer pageNum,Integer pageSize,String preTags,String postTags, Class<T> classType);
    <T> List<T> highlightBuilder(String field,String preTags,String postTags,String text,Class<T> classType);
    List<String> termSuggestion(String fieldName, String text, Class<?> classType);
    <T> List<T> wildcardQuery(String key,String value,Class<T> classType);
    <T> List<T> selectFindPage(String key,Integer pageNum,Integer pageSize,Class<T> classType);
    <T> List<T> multiMatchQuery(String keyword,String[] fields,Class<T> classType);
    <T> Result multiMatchQueryPage(String keyword, Integer pageNum, Integer pageSize, String preTags, String postTags, Class<T> classType);
}
