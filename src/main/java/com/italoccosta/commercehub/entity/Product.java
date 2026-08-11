package com.italoccosta.commercehub.entity;

import com.italoccosta.commercehub.exceptions.InvalidProductException;
import com.italoccosta.commercehub.exceptions.InvalidStockException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String description;
    private BigDecimal price;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;


    public Product(String name, String description, BigDecimal price) {
        this.validateProduct(name,description,price);
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void addStock(Stock stock){
        if(stock == null){
            throw new InvalidStockException("Stock canot be null");
        }
        if(this.stock != null){
            throw new InvalidStockException("Product already has a stock");
        }
        this.stock = stock;
        stock.associateProduct(this);
    }

    private void validateProduct(String name, String description, BigDecimal price) {
        if(name == null ){
            throw new InvalidProductException("Product name can't be null");
        }
        if(description == null ){
            throw new InvalidProductException("Product description can't be null");
        }
        if(price == null ){
            throw new InvalidProductException("Product price can't be null");
        }
        if(name.isBlank()){
            throw new InvalidProductException("Product name can't be blank");
        }
        if(description.isBlank()){
            throw new InvalidProductException("Description can't be blank");
        }
        if(price.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidProductException("Price must be greater than zero");
        }
    }
}
