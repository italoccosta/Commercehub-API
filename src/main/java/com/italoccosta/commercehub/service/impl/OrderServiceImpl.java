package com.italoccosta.commercehub.service.impl;

import com.italoccosta.commercehub.dto.OrderRequest;
import com.italoccosta.commercehub.entity.Customer;
import com.italoccosta.commercehub.entity.Order;
import com.italoccosta.commercehub.entity.OrderItem;
import com.italoccosta.commercehub.exceptions.OrderNotFoundException;
import com.italoccosta.commercehub.exceptions.StockUnavailableException;
import com.italoccosta.commercehub.repository.OrderRepository;
import com.italoccosta.commercehub.service.CustomerService;
import com.italoccosta.commercehub.service.OrderService;
import com.italoccosta.commercehub.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private CustomerService customerService;
    private ProductService productService;
    private OrderRepository orderRepository;

    public OrderServiceImpl(CustomerService customerService, ProductService productService, OrderRepository repository) {
        this.customerService = customerService;
        this.productService = productService;
        this.orderRepository = repository;
    }


    @Override
    public Order createOrder(OrderRequest request) {

        Customer customer = customerService.findCustomerById(request.customerId());

        List<OrderItem> itemsList = request.items().stream()
                .map(item -> new OrderItem(productService.findProductById(item.productId()),
                item.quantity()))
                .toList();

        for(OrderItem item : itemsList){
            if(item.getProduct().getStock() == null){
                throw new StockUnavailableException("Unavailble stock");
            }
            if(item.getQuantity() > item.getProduct().getStock().getQuantity()){
                throw new StockUnavailableException("The order quantity is greater then the available stock");
            }
        }

        Order order = new Order(customer, itemsList);

        for(OrderItem item : order.getItems()){
            item.getProduct().getStock().decreaseQuantity(item.getQuantity());
        }

        orderRepository.save(order);

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Order findOrderById(UUID orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Order not found"));

    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> showAllOrders() {

        return orderRepository.findAll();

    }

}
