package com.devsuperio.dscatalog.contoller;

import com.devsuperio.dscatalog.entity.Category;
import com.devsuperio.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService service;
    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
}
