package com.example.gujitiyao.service;

import com.example.gujitiyao.entity.Book;

import java.util.List;


public interface DataService {
    <T> void saveAll(List<T> entities);
}

