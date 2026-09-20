package com.italoccosta.commercehub.repository;

import com.italoccosta.commercehub.entity.Customer;
import com.italoccosta.commercehub.entity.Order;
import com.italoccosta.commercehub.entity.OrderItem;
import com.italoccosta.commercehub.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void shouldSaveNewOrderSuccessfully(){

       Customer customer = new Customer("Maria Silva",
                "maria@email.com",
                "senha123");

        customerRepository.save(customer);

       Product product1 = new Product(
                "cadeira",
                "Cadeira ergonômica",
                BigDecimal.valueOf(350.0)
        );

       Product product2 = new Product(
                "Carro",
                "Carro de mão",
                BigDecimal.valueOf(25.5));

        productRepository.saveAll(Arrays.asList(product1,product2));

       OrderItem item1 = new OrderItem(product1, 2);
       OrderItem item2 = new OrderItem(product2, 4);

       List<OrderItem> items = Arrays.asList(item1, item2);

        Order order = new Order(customer, items);
        orderRepository.save(order);

        Optional<Order> savedOrder = orderRepository.findById(order.getId());

        assertTrue(savedOrder.isPresent());
        assertEquals(order.getCustomer(), savedOrder.get().getCustomer());
        assertTrue(savedOrder.get().getItems().contains(item1));
        assertTrue(savedOrder.get().getItems().contains(item2));

    }
}
