package com.cosmeticssafety.service;

import java.util.List;

import com.cosmeticssafety.dto.IngredientRequest;
import com.cosmeticssafety.dto.IngredientResponse;

public interface IngredientService {

	IngredientResponse createIngredient(IngredientRequest request);

	IngredientResponse updateIngredient(Long ingredientId, IngredientRequest request);

	List<IngredientResponse> getAllIngredients();
}
