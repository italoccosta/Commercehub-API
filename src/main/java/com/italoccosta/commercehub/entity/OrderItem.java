package com.italoccosta.commercehub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItem(Product product, Integer quantity) {
        validateOrderItem(product, quantity);
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    public BigDecimal getSubtotal() {
        return BigDecimal.valueOf(quantity).multiply(unitPrice);
    }


    private void validateOrderItem(Product product, Integer quantity) {

        if(product == null) {
            throw new IllegalArgumentException("The product can't be null");
        }
        if(quantity == null) {
            throw new IllegalArgumentException("The quantity can't be null");
        }
        if(quantity < 1) {
            throw new IllegalArgumentException("The quantity can't be less than 1");
        }

    }

}
