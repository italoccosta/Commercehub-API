package com.italoccosta.commercehub.entity;

import com.italoccosta.commercehub.exceptions.InvalidProductException;
import com.italoccosta.commercehub.exceptions.InvalidStockException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    void shouldCreateProduct(){
        Product product = new Product(
                "cadeira",
                "Cadeira ergonômica",
                BigDecimal.valueOf(350.00)
        );

        assertEquals("cadeira", product.getName() );
        assertEquals("Cadeira ergonômica", product.getDescription());
        assertEquals(BigDecimal.valueOf(350.00), product.getPrice());
        assertNull(product.getStock());
    }

    @Test
    void shouldRejectBlankName(){

        assertThrows(InvalidProductException.class,
                ()->
                {new Product(
                        "",
                        "Cadeira ergonomica",
                        BigDecimal.valueOf(350.00)
                );});
    }

    @Test
    void shouldRejectBlankDescription(){
        assertThrows(InvalidProductException.class,
                ()->
                {new Product(
                        "Cadeira",
                        "",
                        BigDecimal.valueOf(350.00)
                );});
    }

    @Test
    void shouldRejectNegativePrice(){
        assertThrows(InvalidProductException.class,
                ()->
                {
                    new Product(
                            "Cadeira",
                            "Cadeira ergonômica",
                            BigDecimal.valueOf(-350.00)
                    );});
    }

    @Test
    void shouldRejectZeroPrice(){
        assertThrows(InvalidProductException.class,
                ()->
                {
                     new Product(
                            "Cadeira",
                            "Cadeira ergonômica",
                            BigDecimal.ZERO
                    );});
    }

    @Test
    void shouldRejectNullName(){
        assertThrows(InvalidProductException.class,
                ()->
                {
                    new Product(
                            null,
                            "Cadeira",
                            BigDecimal.valueOf(350.00)
                    );});
    }

    @Test
    void shouldRejectNullDescription(){
        assertThrows(InvalidProductException.class,
                ()->
                {
                     new Product(
                            "Cadeira",
                            null,
                            BigDecimal.valueOf(350.00)
                    );});
    }

    @Test
    void shouldRejectNullPrice(){
        assertThrows(InvalidProductException.class,
                ()->
                {
                     new Product(
                            "Cadeira",
                            "Cadeira ergonomica",
                            null
                    );});
    }

    @Test
    void shouldAddNewStock(){

        Product product = new Product(
                "cadeira",
                "Cadeira ergonômica",
                BigDecimal.valueOf(350.00)
        );
        Stock stock1 = new Stock(product, 50);

        product.addStock(stock1);

        assertNotNull(product.getStock());
        assertEquals(product, stock1.getProduct());
        assertEquals(stock1, product.getStock());
        assertEquals(50, product.getStock().getQuantity());

    }

    @Test
    void shouldRejectSecondStock(){

        Product product = new Product(
                "cadeira",
                "Cadeira ergonômica",
                BigDecimal.valueOf(350.00)
        );
        Stock stock1 = new Stock(product, 50);

        product.addStock(stock1);

        assertThrows(InvalidStockException.class,
                ()->
                {product.addStock(new Stock(product, 60));});
    }

    @Test
    void shouldRejectNullStock(){

        Product product = new Product(
                "cadeira",
                "Cadeira ergonômica",
                BigDecimal.valueOf(350.00)
        );

        assertThrows(InvalidStockException.class,
                ()->
                {product.addStock(null);});
    }
}
