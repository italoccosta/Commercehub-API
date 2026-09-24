package com.italoccosta.commercehub.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRequest(

        @NotNull
        UUID customerId,

        @NotNull
        List<OrderItemRequest> items

) {
}
