package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dtos.CategoryDTO;
import com.devsuperio.dscatalog.entity.Category;
import com.devsuperio.dscatalog.exceptions.EntityNotFoundException;
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
    public List<CategoryDTO> findAll() {
        return repository.findAll().stream().map(CategoryDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found")));
    }
    @Transactional
    public CategoryDTO insert(CategoryDTO dto){
        Category category = new Category();
        category.setName(dto.getName());
        return new CategoryDTO(repository.save(category));
    }
}
