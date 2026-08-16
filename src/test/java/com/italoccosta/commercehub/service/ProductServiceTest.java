package com.italoccosta.commercehub.service;

import com.italoccosta.commercehub.dto.ProductRequest;
import com.italoccosta.commercehub.dto.StockRequest;
import com.italoccosta.commercehub.entity.Product;
import com.italoccosta.commercehub.exceptions.ProductNotFoundException;
import com.italoccosta.commercehub.repository.ProductRepository;
import com.italoccosta.commercehub.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    @Test
    void shouldRegisterProductSuccessfully() {

        ProductRequest productRequest = new ProductRequest("Carro",
                "Carro de mão",
                BigDecimal.valueOf(25));

        Product product = new Product(
                productRequest.name(),
                productRequest.description(),
                productRequest.price());

        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        Product result = productService.registerProduct(productRequest);

        assertEquals(product.getName(), result.getName());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getPrice(), result.getPrice());

        verify(productRepository).save(result);
    }

    @Test
    void shouldReturnProductByIdSuccessfully() {

        UUID productId = UUID.randomUUID();
        Product product = new Product(
                "Carro",
                "Carro de mão",
                BigDecimal.valueOf(25));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        Product foundProduct = productService.findProductById(productId);

        assertEquals(product.getName(), foundProduct.getName());
        assertEquals(product.getDescription(), foundProduct.getDescription());
        assertEquals(product.getPrice(), foundProduct.getPrice());

        verify(productRepository).findById(productId);

    }

    @Test
    void shouldThrowProductNotFoundExceptionWhenFindingById(){

        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.findProductById(productId));

        verify(productRepository).findById(productId);
    }

    @Test
    void shouldReturnAllProductsSuccessfully() {

        Product product1 = new Product("Caneta", "Caneta preta", BigDecimal.valueOf(2));
        Product product2 = new Product("Caneta", "Caneta azul", BigDecimal.valueOf(3));

        when(productRepository.findAll())
                .thenReturn(List.of(product1,product2));

        List<Product> products = productService.findAllProducts();

        assertEquals(2, products.size());
        assertTrue(products.contains(product1));
        assertTrue(products.contains(product2));

        verify(productRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenFindingAllProducts(){

        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> products = productService.findAllProducts();

        assertTrue(products.isEmpty());
        verify(productRepository).findAll();
    }

    @Test
    void shouldUpdateProductSuccessfully() {

        UUID productId = UUID.randomUUID();
        Product product = new Product(
                "Carro",
                "Carro de mão",
                BigDecimal.valueOf(25));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        Product updatedProduct = productService.updateProduct(productId,
                new ProductRequest("Caneta",
                        "Caneta preta",
                        BigDecimal.valueOf(2)));

        assertEquals("Caneta", updatedProduct.getName());
        assertEquals("Caneta preta", updatedProduct.getDescription());
        assertEquals(BigDecimal.valueOf(2), updatedProduct.getPrice());

        verify(productRepository).findById(productId);
    }

    @Test
    void shouldDeleteProductSuccessfully() {

        UUID productId = UUID.randomUUID();
        Product product1 = new Product("Caneta",
                "Caneta preta",
                BigDecimal.valueOf(2));

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product1));

        productService.deleteProduct(productId);

        verify(productRepository).findById(productId);
        verify(productRepository).delete(product1);
    }

    @Test
    void shouldRegisterStockSuccessfully(){

        UUID productId = UUID.randomUUID();
        Product product = new Product(
                "Carro",
                "Carro de mão",
                BigDecimal.valueOf(25));

        StockRequest stockRequest = new StockRequest(product, 50);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        productService.registerStock(productId, stockRequest);

        assertNotNull(product.getStock());
        assertEquals(50, product.getStock().getQuantity());
        assertEquals(product, product.getStock().getProduct());

        verify(productRepository).findById(productId);
    }

    @Test
    void shouldIncreaseStockSuccessfully(){

        UUID productId = UUID.randomUUID();
        Product product = new Product(
                "Carro",
                "Carro de mão",
                BigDecimal.valueOf(25));
        StockRequest stockRequest = new StockRequest(product, 50);

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        productService.registerStock(productId, stockRequest);
        productService.increaseStock(productId, 50);

        assertEquals(100, product.getStock().getQuantity());

        verify(productRepository, times(2))
                .findById(productId);
    }

    @Test
    void shouldRemoveStockSuccessfully(){

        UUID productId = UUID.randomUUID();
        Product product = new Product(
                "Carro",
                "Carro de mão",
                BigDecimal.valueOf(25));
        StockRequest stockRequest = new StockRequest(product, 50);


        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));
        productService.registerStock(productId, stockRequest);
        productService.removeStock(productId);

        assertNull(product.getStock());
        verify(productRepository, times(2))
                .findById(productId);

    }
}
