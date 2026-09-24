package com.italoccosta.commercehub.service;

import com.italoccosta.commercehub.dto.OrderRequest;
import com.italoccosta.commercehub.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(OrderRequest request);
    Order findOrderById(UUID orderId);
    List<Order> showAllOrders();

}
