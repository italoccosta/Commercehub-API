package com.italoccosta.commercehub.entity;

import com.italoccosta.commercehub.exceptions.InvalidOrderException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderItem> items;

    private Instant createdAt;

    public Order(Customer customer, List<OrderItem> items) {

        validateOrder(customer, items);
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.createdAt = Instant.now();

    }

    public BigDecimal getTotalCost() {

        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public List<OrderItem> getItems() {

        return Collections.unmodifiableList(items);

    }

    private void validateOrder(Customer customer, List<OrderItem> items) {

        if(customer == null) {
            throw new InvalidOrderException("The customer can't be null");
        }
        if(items == null) {
            throw new InvalidOrderException("The items can't be null");
        }
        if(items.isEmpty()) {
            throw new InvalidOrderException("The items can't be empty");
        }

    }

}
