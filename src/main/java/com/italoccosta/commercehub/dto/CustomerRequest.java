package com.italoccosta.commercehub.dto;

public record CustomerRequest(
        String name,
        String email,
        String password
) {
}
