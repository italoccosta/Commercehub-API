package com.italoccosta.commercehub.entity;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;



public class OrderItemTest {

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
    void createOrderItemSuccessfully(){

        OrderItem item = new OrderItem(product, 1);

        assertEquals(product, item.getProduct());
        assertEquals(1, item.getQuantity());
        assertEquals(product.getPrice(), item.getUnitPrice());

    }

    @Test
    void shouldRejectNullProduct(){

       assertThrows(IllegalArgumentException.class,
               ()-> new OrderItem(null, 3));

    }

    @Test
    void shouldRejectNullQuantity() {

        assertThrows(IllegalArgumentException.class,
                ()-> new OrderItem(product,null));

    }

    @Test
    void shouldRejectQuantityZero(){

        assertThrows(IllegalArgumentException.class,
                ()-> new OrderItem(product,0));

    }

    @Test
    void shouldRejectNegativeQuantity(){

        assertThrows(IllegalArgumentException.class,
                ()-> new OrderItem(product,-2));

    }

    @Test
    void shouldCalculateSubtotalSuccessfully(){

        OrderItem item = new OrderItem(product,4);

        assertEquals(BigDecimal.valueOf(1400.0), item.getSubtotal());

    }

}
