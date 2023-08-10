package com.devsuperio.dscatalog.controller;

import com.devsuperio.dscatalog.Factory;
import com.devsuperio.dscatalog.contoller.ProductController;
import com.devsuperio.dscatalog.dtos.ProductDTO;
import com.devsuperio.dscatalog.exceptions.ResourceNotFoundException;
import com.devsuperio.dscatalog.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper mapper;
    private PageImpl<ProductDTO> page;
    private long existingId;
    private long nonExistingId;
    private ProductDTO productDTO;

    @BeforeEach
    void setup() throws Exception {
        productDTO = Factory.createProductDTO();
        existingId = 1L;
        nonExistingId = 2L;
        page = new PageImpl<>(List.of(productDTO));
        Mockito.when(service.findAll(any())).thenReturn(page);
        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).findById(nonExistingId);

        Mockito.when(service.update(existingId, eq(any()))).thenReturn(productDTO);
        Mockito.when(service.update(nonExistingId, eq(any()))).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    public void findAllShouldReturnAPage() throws Exception {
        ResultActions result = mockMvc.perform(get("/products"));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShoulReturnProductWhenIdExistis() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void findByIdShoulReturnNotFoundExceptionWhenIdNotExists() throws Exception {
        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());

    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdNotExists() throws Exception {

        String jsonBody = mapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());
    }
}
