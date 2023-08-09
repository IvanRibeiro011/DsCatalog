package com.devsuperio.dscatalog.repositories;

import com.devsuperio.dscatalog.Factory;
import com.devsuperio.dscatalog.entity.Product;
import com.devsuperio.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository repository ;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;
    @BeforeEach
    void setup() {
        existingId = 1L;
        nonExistingId = 100L;
        countTotalProducts = 25L;
    }
    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);
        Assertions.assertFalse(repository.findById(existingId).isPresent());
    }
    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdNull(){
        Product product = Factory.created();
        product.setId(null);
        repository.save(product);
        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1,product.getId());
    }
    @Test
    public void findByIdShouldReturnOptionalNotEmptyWhenIdExists(){
        Assertions.assertTrue(repository.findById(existingId).isPresent());
    }
    @Test
    public void findByIdShouldReturnOptionalEmptyWhenIdExists(){
        Assertions.assertFalse(repository.findById(nonExistingId).isPresent());
    }
}

