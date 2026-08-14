package com.italoccosta.commercehub.service;

import com.italoccosta.commercehub.dto.ProductRequest;
import com.italoccosta.commercehub.dto.StockRequest;
import com.italoccosta.commercehub.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

     Product registerProduct(ProductRequest productRequest);
     Product findProductById(UUID productId);
     List<Product> findAllProducts();
     Product updateProduct(UUID productId, ProductRequest productRequest);
     void deleteProduct(UUID productId);
     void registerStock(UUID productId, StockRequest stock);
     void increaseStock(UUID productId, Integer quantity);
     void removeStock(UUID productId);

}
