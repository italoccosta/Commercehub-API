package com.italoccosta.commercehub.entity;

import com.italoccosta.commercehub.exceptions.InvalidOrderException;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Customer customer;
    private Product product1;
    private Product product2;
    private OrderItem item1;
    private OrderItem item2;
    private List<OrderItem> items;

    @BeforeEach
    void setup(){

        customer = new Customer("Maria Silva",
                "maria@email.com",
                "senha123");

        product1 = new Product(
                "cadeira",
                "Cadeira ergonômica",
                BigDecimal.valueOf(350.0)
        );

        product2 = new Product(
                "Carro",
                "Carro de mão",
                BigDecimal.valueOf(25.5));

        item1 = new OrderItem(product1, 2);
        item2 = new OrderItem(product2, 4);

        items = List.of(item1, item2);
    }

    @Test
    void shouldCreateOrderSuccessfully(){

        Order order = new Order(customer, items);

        assertNotNull(order.getCreatedAt());
        assertEquals(customer, order.getCustomer());
        assertEquals(2, order.getItems().size());
        assertTrue(order.getItems().contains(item1));
        assertTrue(order.getItems().contains(item2));

    }

    @Test
    void shouldRejectNullCustomer(){

        assertThrows(InvalidOrderException.class,
                ()-> new Order(null, items));

    }

    @Test
    void shouldRejectNullItems(){

        assertThrows(InvalidOrderException.class,
                ()-> new Order(customer, null));
    }

    @Test
    void shouldRejectEmptyItemsList(){

        assertThrows(InvalidOrderException.class,
                ()-> new Order(customer, Collections.emptyList()));

    }

    @Test
    void shouldReturnTheTotalCoastOfThisOrder(){

        Order order = new Order(customer, items);

        assertEquals(BigDecimal.valueOf(802.0), order.getTotalCost());
    }

    @Test
    void shouldKeepInternalItemsListIndependentFromOriginalList(){

        List<OrderItem> originalList = new ArrayList<>();
        originalList.add(item1);

        Order newOrder = new Order(customer, originalList);
        originalList.add(item2);

        assertFalse(newOrder.getItems().contains(item2));

    }

    @Test
    void shouldPreventModificationOfItemsList(){

        List<OrderItem> originalList = new ArrayList<>();
        originalList.add(item1);

        Order newOrder = new Order(customer, originalList);

        assertThrows(UnsupportedOperationException.class,
                ()-> newOrder.getItems().add(item2));

    }

}
