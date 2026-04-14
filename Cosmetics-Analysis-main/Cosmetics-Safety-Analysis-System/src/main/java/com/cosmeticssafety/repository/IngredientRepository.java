package com.cosmeticssafety.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cosmeticssafety.common.enums.IngredientRiskLevel;
import com.cosmeticssafety.entity.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

	Optional<Ingredient> findByNameIgnoreCase(String name);

	List<Ingredient> findAllByOrderByCreatedAtDesc();

	List<Ingredient> findAllByRiskLevelOrderByCreatedAtDesc(IngredientRiskLevel riskLevel);
}
