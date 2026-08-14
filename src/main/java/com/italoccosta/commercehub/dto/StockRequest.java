package com.italoccosta.commercehub.dto;

import com.italoccosta.commercehub.entity.Product;

public record StockRequest(
        Product product,
        Integer quantity
) {
}
