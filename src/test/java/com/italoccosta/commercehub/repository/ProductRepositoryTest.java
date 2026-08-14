package com.italoccosta.commercehub.repository;

import com.italoccosta.commercehub.entity.Product;
import static org.junit.jupiter.api.Assertions.*;


import com.italoccosta.commercehub.entity.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private StockRepository stockRepository;


    @Test
    void shouldSaveProduct() {

        Product product = new Product(
                "Caneta",
                "Caneta preta",
                BigDecimal.valueOf(2)
        );

        repository.save(product);
        Optional<Product> savedProduct = repository.findById(product.getId());

        assertTrue(savedProduct.isPresent());
        assertEquals("Caneta", savedProduct.get().getName());
        assertEquals("Caneta preta", savedProduct.get().getDescription());
        assertEquals(BigDecimal.valueOf(2), savedProduct.get().getPrice());
    }

    @Test
    void shouldReturnEmptyWhenProductDoesNotExist(){

        Optional<Product> emptyProduct = repository
                .findById(UUID.randomUUID());

        assertTrue(emptyProduct.isEmpty());
    }

    @Test
    void shouldReturnAllProducts(){

        Product product1 = new Product("Caneta", "Caneta preta", BigDecimal.valueOf(2));
        Product product2 = new Product("Caneta", "Caneta azul", BigDecimal.valueOf(3));
        Product product3 = new Product("Caneta", "Caneta vermelha", BigDecimal.valueOf(2.5));

        List<Product> products = List.of(product1,product2,product3);

        repository.saveAll(products);

        List<Product> savedProducts = repository.findAll();

        assertEquals(3, savedProducts.size());
        assertTrue(savedProducts.contains(product1));
        assertTrue(savedProducts.contains(product2));
        assertTrue(savedProducts.contains(product3));
    }

    @Test
    void shouldUpdateProduct(){

        Product product1 = new Product("Caneta",
                "Caneta preta",
                BigDecimal.valueOf(2));

        repository.save(product1);
        product1.updateProduct("Carrinho",
                "Carrinho de brinquedo",
                BigDecimal.valueOf(10));

        Optional<Product> updatedProduct = repository.findById(product1.getId());

        assertTrue(updatedProduct.isPresent());
        assertEquals("Carrinho", updatedProduct.get().getName());
        assertEquals("Carrinho de brinquedo", updatedProduct.get().getDescription());
        assertEquals(BigDecimal.valueOf(10), updatedProduct.get().getPrice());

    }

    @Test
    void shouldSaveProductWithStock(){

        Product product1 = new Product("Caneta", "Caneta azul", BigDecimal.valueOf(2));
        Stock stock = new Stock(product1, 50);
        product1.addStock(stock);
        repository.save(product1);

        Optional<Product> savedProduct = repository.findById(product1.getId());

        assertTrue(savedProduct.isPresent());
        assertEquals(50, savedProduct.get().getStock().getQuantity());
        assertEquals(savedProduct.get().getStock().getId(), stock.getId());
        assertEquals(savedProduct.get(), savedProduct.get().getStock().getProduct());
    }

    @Test
    void shouldRemoveStockWhenProductStockIsRemoved(){

        Product product1 = new Product("Caneta", "Caneta azul", BigDecimal.valueOf(2));
        Stock stock = new Stock(product1, 50);
        product1.addStock(stock);
        repository.save(product1);
        repository.flush();

        product1.removeStock();
        repository.flush();

        Optional<Stock> removedStock = stockRepository.findById(stock.getId());
        Optional<Product> savedProduct = repository.findById(product1.getId());

        assertTrue(removedStock.isEmpty());
        assertEquals(null, savedProduct.get().getStock());

    }

    @Test
    void shouldRemoveProductAndStock(){
        Product product = new Product("Caneta",
                "Caneta azul",
                BigDecimal.valueOf(2));

        Stock stock = new Stock(product, 50);
        product.addStock(stock);

        repository.save(product);
        repository.flush();

        UUID productId = product.getId();
        UUID stockId = stock.getId();

        repository.delete(product);
        repository.flush();

        Optional<Product> removedProduct = repository.findById(productId);
        Optional<Stock> removedStock = stockRepository.findById(stockId);

        assertTrue(removedProduct.isEmpty());
        assertTrue(removedStock.isEmpty());
    }
}
