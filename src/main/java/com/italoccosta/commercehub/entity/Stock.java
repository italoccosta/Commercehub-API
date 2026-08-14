package com.italoccosta.commercehub.entity;

import com.italoccosta.commercehub.exceptions.InvalidStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class Stock {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    public Stock(Product product, Integer quantity){
        if(product == null){
            throw new InvalidStockException("Product cannot be null");
        }
        if(quantity == null){
            throw new InvalidStockException("Quantity cannot be null");
        }
        if(quantity < 0){
            throw  new InvalidStockException("Stock quantity cannot be negative");
        }
        this.product = product;
        this.quantity = quantity;
    }

    public void increaseQuantity(Integer quantity){
        if(quantity == null){
            throw  new InvalidStockException("Stock quantity cannot be null");
        }
        if(quantity <= 0){
            throw  new InvalidStockException("Quantity to increase must be greater than zero");
        }
        this.quantity += quantity;
    }

    public void decreaseQuantity(Integer quantity){
        if(quantity == null){
            throw  new InvalidStockException("Quantity to decrease cannot be null");
        }
        if(quantity <= 0){
            throw  new InvalidStockException("Quantity to decrease must be greater than zero");
        }
        if(this.quantity < quantity){
            throw  new InvalidStockException("Quantity to decrease cannot exceed available stock");
        }
        this.quantity -= quantity;
    }

}
