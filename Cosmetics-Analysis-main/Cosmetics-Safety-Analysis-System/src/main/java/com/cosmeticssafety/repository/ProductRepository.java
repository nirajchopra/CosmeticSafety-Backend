package com.cosmeticssafety.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cosmeticssafety.entity.Brand;
import com.cosmeticssafety.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findAllByOrderByCreatedAtDesc();

	List<Product> findAllByBrandOrderByCreatedAtDesc(Brand brand);
}
