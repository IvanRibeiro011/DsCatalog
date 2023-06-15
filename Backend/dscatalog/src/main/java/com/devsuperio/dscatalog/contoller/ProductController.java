package com.devsuperio.dscatalog.contoller;

import com.devsuperio.dscatalog.dtos.ProductDTO;
import com.devsuperio.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable){
        return new ResponseEntity<>(service.findAll(pageable), HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable("id") Long id){
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto){
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(service.insert(dto));
    }
    @PutMapping("{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable("id")Long id,
                                              @RequestBody ProductDTO dto){
        return new ResponseEntity<>(service.update(id,dto),HttpStatus.OK);
    }
    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id")Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
