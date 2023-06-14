package com.devsuperio.dscatalog.contoller;

import com.devsuperio.dscatalog.dtos.CategoryDTO;
import com.devsuperio.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService service;
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(service.insert(dto));
    }
    @PutMapping("{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable("id")Long id,
                                              @RequestBody CategoryDTO dto){
        return new ResponseEntity<>(service.update(id,dto),HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id")Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
