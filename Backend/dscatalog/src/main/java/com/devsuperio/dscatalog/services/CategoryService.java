package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.entity.Category;
import com.devsuperio.dscatalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;
    @Transactional(readOnly = true)
    public List<Category> findAll(){
        return repository.findAll();
    }
}
