package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dtos.ProductDTO;
import com.devsuperio.dscatalog.dtos.ProductDTO;
import com.devsuperio.dscatalog.entity.Product;
import com.devsuperio.dscatalog.exceptions.DatabaseException;
import com.devsuperio.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperio.dscatalog.repository.ProductRepository;
import com.devsuperio.dscatalog.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product =repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(product,product.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product Product = new Product();
//        Product.setName(dto.getName());
        return new ProductDTO(repository.save(Product));
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product Product = repository.getReferenceById(id);
            Product.setName(dto.getName());
            return new ProductDTO(repository.save(Product));
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Entity not found");
        }
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
