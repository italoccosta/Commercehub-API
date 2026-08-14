package com.italoccosta.commercehub.entity;

import com.italoccosta.commercehub.exceptions.InvalidStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

    private Product product;

    @BeforeEach
    void setup(){
        product = new Product(
                "cadeira",
                "Cadeira ergonômica",
                BigDecimal.valueOf(350.00)
        );
    }

    @Test
    void shouldCreateValidStock(){

        Stock stock = new Stock(product, 50);

        assertEquals(product, stock.getProduct());
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void shouldCreateValidStockWithZeroQuantity(){

        Stock stock = new Stock(product, 0);

        assertEquals(product, stock.getProduct());
        assertEquals(0, stock.getQuantity());

    }

    @Test
    void shouldRejectNullProduct(){
        assertThrows(InvalidStockException.class,
                ()-> new Stock(null, 50));
    }

    @Test
    void shouldRejectNullQuantity(){
        assertThrows(InvalidStockException.class,
                ()-> new Stock(product, null));
    }

    @Test
    void shouldRejectNegativeQuantity(){
        assertThrows(InvalidStockException.class,
                ()-> new Stock(product, -1));
    }

    @Test
    void shouldIncreaseStockQuantity(){

        Stock stock = new Stock(product, 50);
        stock.increaseQuantity(10);

        assertEquals(60, stock.getQuantity());
    }

    @Test
    void shouldRejectIncreaseZero(){

        Stock stock = new Stock(product, 50);

        assertThrows(InvalidStockException.class,
                ()-> stock.increaseQuantity(0));
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void shouldRejectIncreaseNegativeQuantity(){

        Stock stock = new Stock(product, 50);

        assertThrows(InvalidStockException.class,
                ()-> stock.increaseQuantity(-10));
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void shouldRejectIncreaseNull(){

        Stock stock = new Stock(product, 50);

        assertThrows(InvalidStockException.class,
                ()-> stock.increaseQuantity(null));
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void shouldDecreaseStockQuantity(){
        Stock stock = new Stock(product, 50);
        stock.decreaseQuantity(10);

        assertEquals(40, stock.getQuantity());
    }

    @Test
    void shouldDecreaseStockQuantityToZero(){
        Stock stock = new Stock(product, 50);
        stock.decreaseQuantity(50);

        assertEquals(0, stock.getQuantity());
    }

    @Test
    void shouldRejectDecreaseZero(){
        Stock stock = new Stock(product, 50);

        assertThrows(InvalidStockException.class,
                ()-> stock.decreaseQuantity(0));
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void shouldRejectDecreaseNegativeQuantity(){
        Stock stock = new Stock(product, 50);

        assertThrows(InvalidStockException.class,
                ()-> stock.decreaseQuantity(-10));
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void shouldRejectDecreaseNull(){

        Stock stock = new Stock(product, 50);

        assertThrows(InvalidStockException.class,
                ()-> stock.decreaseQuantity(null));
        assertEquals(50, stock.getQuantity());
    }

    @Test
    void shouldRejectDecreaseQuantityGreaterThanAvailableStock(){

        Stock stock = new Stock(product, 50);

        assertThrows(InvalidStockException.class,
                ()-> stock.decreaseQuantity(60));
        assertEquals(50, stock.getQuantity());
    }
}
