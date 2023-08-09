package com.devsuperio.dscatalog;

import com.devsuperio.dscatalog.dtos.ProductDTO;
import com.devsuperio.dscatalog.entity.Category;
import com.devsuperio.dscatalog.entity.Product;

import java.time.Instant;

public class Factory {

    public static Product created() {
        Product product = new Product(1L, "Phone", "Good phone", 800.0, "", Instant.parse("2020-05-06T03:00:00Z"));
        product.getCategories().add(new Category(1L, "Eletronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){
        Product product = created();
        return new ProductDTO(product,product.getCategories());
    }

    public static Category createCategory(){
        Category category = new Category(1L,"Movies");
        return category;
    }
}
