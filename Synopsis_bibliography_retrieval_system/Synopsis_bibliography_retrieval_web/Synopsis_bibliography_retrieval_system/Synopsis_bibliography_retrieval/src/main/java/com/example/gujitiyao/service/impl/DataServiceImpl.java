package com.example.gujitiyao.service.impl;

import com.example.gujitiyao.dao.EntityRepository;
import com.example.gujitiyao.dao.FigureRepository;
import com.example.gujitiyao.dao.UserRepository;
import com.example.gujitiyao.entity.Book;
import com.example.gujitiyao.entity.Entity;
import com.example.gujitiyao.entity.Figure;
import com.example.gujitiyao.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FigureRepository figureRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Override
    public <T> void saveAll(List<T> entities){
        if (!entities.isEmpty()) {
            T firstEntity = entities.get(0);
            if (firstEntity instanceof Book) {
                userRepository.saveAll((List<Book>) entities);
            } else if (firstEntity instanceof Figure) {
                figureRepository.saveAll((List<Figure>) entities);
            } else if (firstEntity instanceof Entity) {
                entityRepository.saveAll((List<Entity>) entities);
            } else {
                throw new IllegalArgumentException("Unsupported entity type");
            }
        }
    }


}
