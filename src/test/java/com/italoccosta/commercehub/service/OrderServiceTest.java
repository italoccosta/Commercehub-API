package com.italoccosta.commercehub.service;

import com.italoccosta.commercehub.dto.OrderItemRequest;
import com.italoccosta.commercehub.dto.OrderRequest;
import com.italoccosta.commercehub.entity.*;
import com.italoccosta.commercehub.exceptions.CustomerNotFoundException;
import com.italoccosta.commercehub.exceptions.OrderNotFoundException;
import com.italoccosta.commercehub.exceptions.ProductNotFoundException;
import com.italoccosta.commercehub.exceptions.StockUnavailableException;
import com.italoccosta.commercehub.repository.OrderRepository;
import com.italoccosta.commercehub.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    private Customer customer;
    private Product product1;
    private Product product2;
    private Stock stock1;
    private Stock stock2;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setup(){

        customer = new Customer(
                "Luiz Costa",
                "Luiz@email.com",
                "teste123"
        );

        product1 = new Product(
                "Carro",
                "Carro de mão",
                BigDecimal.valueOf(25));

        stock1 = new Stock(product1, 30);
        product1.addStock(stock1);

        product2 = new Product("Caneta",
                "Caneta preta",
                BigDecimal.valueOf(2));

        stock2 = new Stock(product2, 30);
        product2.addStock(stock2);

    }


    @Test
    void shouldCreateOrderSuccessfully(){

        UUID customerId = UUID.randomUUID();
        UUID product1Id = UUID.randomUUID();
        UUID product2Id = UUID.randomUUID();

        OrderItemRequest orderItemRequest1 = new OrderItemRequest(product1Id, 6);
        OrderItemRequest orderItemRequest2 = new OrderItemRequest(product2Id, 18);

        OrderRequest orderRequest = new OrderRequest(customerId,
                List.of(orderItemRequest1,orderItemRequest2));

        when(customerService.findCustomerById(customerId))
                .thenReturn(customer);
        when(productService.findProductById(product1Id))
                .thenReturn(product1);
        when(productService.findProductById(product2Id))
                .thenReturn(product2);

        Order result = orderService.createOrder(orderRequest);

        OrderItem item1 = result.getItems().get(0);
        OrderItem item2 = result.getItems().get(1);

        assertNotNull(result);
        assertEquals(customer, result.getCustomer());
        assertEquals(2, result.getItems().size());
        assertEquals(product1, item1.getProduct());
        assertEquals(product2, item2.getProduct());
        assertEquals(6, item1.getQuantity());
        assertEquals(18, item2.getQuantity());
        assertEquals(BigDecimal.valueOf(25), item1.getUnitPrice());
        assertEquals(BigDecimal.valueOf(2), item2.getUnitPrice());
        assertEquals(24, stock1.getQuantity());
        assertEquals(12, stock2.getQuantity());
        assertEquals(BigDecimal.valueOf(186), result.getTotalCost());

        verify(orderRepository).save(result);
        verify(customerService).findCustomerById(customerId);
        verify(productService).findProductById(product1Id);
        verify(productService).findProductById(product2Id);

    }

    @Test
    void shouldRejectCustomerNotFound(){

        UUID customerId = UUID.randomUUID();
        UUID product1Id = UUID.randomUUID();

        when(customerService.findCustomerById(customerId))
                .thenThrow(CustomerNotFoundException.class);

        OrderItemRequest orderItemRequest1 = new OrderItemRequest(product1Id, 6);

        OrderRequest orderRequest = new OrderRequest(customerId,
                List.of(orderItemRequest1));

        assertThrows(CustomerNotFoundException.class,
                ()-> orderService.createOrder(orderRequest));

        verify(orderRepository, never()).save(any());

    }

    @Test
    void shouldRejectProductNotFound(){

        UUID customerId = UUID.randomUUID();
        UUID product1Id = UUID.randomUUID();

        when(customerService.findCustomerById(customerId))
                .thenReturn(customer);

        when(productService.findProductById(product1Id))
                .thenThrow(ProductNotFoundException.class);

        OrderItemRequest orderItemRequest1 = new OrderItemRequest(product1Id, 6);
        OrderRequest orderRequest = new OrderRequest(customerId,
                List.of(orderItemRequest1));

        assertThrows(ProductNotFoundException.class,
                ()-> orderService.createOrder(orderRequest));

        verify(orderRepository, never()).save(any());

    }

    @Test
    void shouldRejectProductWithoutStock(){

        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        Product product = new Product("Lapis",
                "Lapis comum",
                BigDecimal.valueOf(2));

        when(customerService.findCustomerById(customerId))
                .thenReturn(customer);

        when(productService.findProductById(productId))
                .thenReturn(product);

        OrderItemRequest orderItemRequest1 = new OrderItemRequest(productId, 6);
        OrderRequest orderRequest = new OrderRequest(customerId,
                List.of(orderItemRequest1));

        assertThrows(StockUnavailableException.class,
                ()-> orderService.createOrder(orderRequest));

        verify(orderRepository, never()).save(any());

    }

    @Test
    void shouldRejectInsufficientStock(){

        UUID customerId = UUID.randomUUID();
        UUID product1Id = UUID.randomUUID();

        when(customerService.findCustomerById(customerId))
                .thenReturn(customer);
        when(productService.findProductById(product1Id))
                .thenReturn(product1);

        OrderItemRequest orderItemRequest = new OrderItemRequest(product1Id, 31);
        OrderRequest orderRequest = new OrderRequest(customerId,
                List.of(orderItemRequest));

        assertThrows(StockUnavailableException.class,
                ()-> orderService.createOrder(orderRequest));

        verify(orderRepository, never()).save(any());

    }

    @Test
    void shouldFindOrderByIdSuccessfully(){

        UUID orderId = UUID.randomUUID();
        OrderItem item1 = new OrderItem(product1, 6);
        OrderItem item2 = new OrderItem(product2, 8);

        Order order = new Order(customer, List.of(item1,item2));

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        Order result = orderService.findOrderById(orderId);

        assertNotNull(result);
        assertEquals(order, result);

        verify(orderRepository).findById(orderId);

    }

    @Test
    void shouldReturnOrderNotFoundExceptionWhenFindingById(){

        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class,
                ()-> orderService.findOrderById(orderId));

        verify(orderRepository).findById(orderId);

    }

    @Test
    void shouldReturnAllOrdersSuccessfully(){

        OrderItem item1 = new OrderItem(product1, 6);
        OrderItem item2 = new OrderItem(product2, 8);
        OrderItem item3 = new OrderItem(product1, 8);
        OrderItem item4 = new OrderItem(product2, 4);

        Order order1 = new Order(customer, List.of(item1,item2));
        Order order2 = new Order(customer, List.of(item3,item4));

        when(orderRepository.findAll()).thenReturn(List.of(order1,order2));

        List<Order> orders = orderService.showAllOrders();

        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));

        verify(orderRepository).findAll();

    }

    @Test
    void shouldReturnEmptyListWhenFindingAllOrders(){

        when(orderRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<Order> orders = orderService.showAllOrders();

        assertTrue(orders.isEmpty());

        verify(orderRepository).findAll();

    }

}
