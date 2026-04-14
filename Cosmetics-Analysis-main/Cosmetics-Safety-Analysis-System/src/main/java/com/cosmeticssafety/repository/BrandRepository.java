package com.cosmeticssafety.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cosmeticssafety.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	Optional<Brand> findByNameIgnoreCase(String name);
}
