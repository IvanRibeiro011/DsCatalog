package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.dtos.ProductDTO;
import com.devsuperio.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperio.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIT {
	@Autowired
	private ProductService service;
	@Autowired
	private ProductRepository repository;
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;

	@BeforeEach
	public void setup() {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	@Test
	public void findAllPageShouldReturnPageWhenPage0Size10(){
		Pageable pageable = PageRequest.of(0,10);
		Page<ProductDTO> result = service.findAll(pageable);

		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0,result.getNumber());
		Assertions.assertEquals(10,result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	@Test
	public void findAllPageShouldReturnEmptyPageWhenPageNotExists(){
		Pageable pageable = PageRequest.of(50,10);
		Page<ProductDTO> result = service.findAll(pageable);

		Assertions.assertTrue(result.isEmpty());
	}
	@Test
	public void findAllPageShouldReturnSortedPageWhenSortByName(){
		Pageable pageable = PageRequest.of(0,10, Sort.by("name"));
		Page<ProductDTO> result = service.findAll(pageable);

		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("MacBook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("Pc Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("Pc Gamer Alfa", result.getContent().get(2).getName());
	}

	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
			service.delete(existingId);

		Assertions.assertEquals(countTotalProducts - 1,repository.count());
	}
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExists(){
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.delete(nonExistingId);
		});
	}
}
