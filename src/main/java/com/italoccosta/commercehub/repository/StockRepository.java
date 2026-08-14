package com.italoccosta.commercehub.repository;

import com.italoccosta.commercehub.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
}
