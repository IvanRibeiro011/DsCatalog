package com.devsuperio.dscatalog.services;

import com.devsuperio.dscatalog.Factory;
import com.devsuperio.dscatalog.dtos.ProductDTO;
import com.devsuperio.dscatalog.entity.Category;
import com.devsuperio.dscatalog.entity.Product;
import com.devsuperio.dscatalog.exceptions.DatabaseException;
import com.devsuperio.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperio.dscatalog.repository.CategoryRepository;
import com.devsuperio.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	@InjectMocks
	private ProductService productService;
	@Mock
	private ProductRepository repository;
	@Mock
	private CategoryRepository categoryRepository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;

	private long categoryId;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO productDto;
	private Category category;

	@BeforeEach
	public void setup() {
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		categoryId = 1L;
		product = Factory.created();
		productDto = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		doNothing().when(repository).deleteById(existingId);
		doThrow(ResourceNotFoundException.class).when(repository).deleteById(nonExistingId);
		doThrow(DatabaseException.class).when(repository).deleteById(dependentId);

		when(repository.existsById(existingId)).thenReturn(true);
		when(repository.existsById(nonExistingId)).thenReturn(false);
		when(repository.existsById(dependentId)).thenReturn(true);

		when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		when(repository.getReferenceById(existingId)).thenReturn(product);
		when(categoryRepository.getReferenceById(categoryId)).thenReturn(category);
		doThrow(ResourceNotFoundException.class).when(repository).getReferenceById(nonExistingId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			productService.delete(existingId);
		});
		verify(repository, times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			productService.delete(nonExistingId);
		});
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			productService.delete(dependentId);
		});
	}

	@Test
	public void findAllShouldReturnPageable() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = productService.findAll(pageable);
		Assertions.assertNotNull(result);
		verify(repository, Mockito.times(1)).findAll(pageable);
	}

	@Test
	public void findByIdShouldReturnProductDtoWhenIdExists(){
		productDto = productService.findById(existingId);
		Assertions.assertNotNull(productDto);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists(){
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			productService.findById(nonExistingId);
		});
	}

	@Test
	public void updateShouldReturnProductDtoWhenIdExists(){
		Assertions.assertNotNull(productService.update(existingId,productDto));
		Mockito.verify(repository,Mockito.times(1)).getReferenceById(existingId);
		Mockito.verify(categoryRepository,Mockito.times(1)).getReferenceById(existingId);
	}
	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdNotExists(){
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			productService.update(nonExistingId,productDto);
		});
	}
}
