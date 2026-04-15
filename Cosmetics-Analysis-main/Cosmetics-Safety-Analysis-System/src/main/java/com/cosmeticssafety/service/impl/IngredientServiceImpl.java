package com.cosmeticssafety.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmeticssafety.dto.IngredientRequest;
import com.cosmeticssafety.dto.IngredientResponse;
import com.cosmeticssafety.entity.Ingredient;
import com.cosmeticssafety.exception.ResourceNotFoundException;
import com.cosmeticssafety.repository.IngredientRepository;
import com.cosmeticssafety.service.IngredientService;

@Service
public class IngredientServiceImpl implements IngredientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IngredientServiceImpl.class);

	private final IngredientRepository ingredientRepository;

	public IngredientServiceImpl(IngredientRepository ingredientRepository) {
		this.ingredientRepository = ingredientRepository;
	}

	@Override
	@Transactional
	public IngredientResponse createIngredient(IngredientRequest request) {
		ingredientRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
			LOGGER.warn("Ingredient creation blocked because name already exists: {}", request.getName());
			throw new IllegalArgumentException("Ingredient already exists with name: " + request.getName());
		});
		Ingredient ingredient = new Ingredient();
		applyIngredientValues(ingredient, request);
		Ingredient savedIngredient = ingredientRepository.saveAndFlush(ingredient);
		LOGGER.info("Created ingredient with id={} and name={}", savedIngredient.getId(), savedIngredient.getName());
		return mapToResponse(savedIngredient);
	}

	@Override
	@Transactional
	public IngredientResponse updateIngredient(Long ingredientId, IngredientRequest request) {
		Ingredient ingredient = ingredientRepository.findById(ingredientId)
				.orElseThrow(() -> new ResourceNotFoundException("Ingredient not found with id: " + ingredientId));
		applyIngredientValues(ingredient, request);
		Ingredient savedIngredient = ingredientRepository.saveAndFlush(ingredient);
		LOGGER.info("Updated ingredient with id={}", savedIngredient.getId());
		return mapToResponse(savedIngredient);
	}

	@Override
	@Transactional(readOnly = true)
	public List<IngredientResponse> getAllIngredients() {
		return ingredientRepository.findAllByOrderByCreatedAtDesc().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	private void applyIngredientValues(Ingredient ingredient, IngredientRequest request) {
		ingredient.setName(request.getName());
		ingredient.setScientificName(request.getScientificName());
		ingredient.setRiskLevel(request.getRiskLevel());
		ingredient.setAllowedInCosmetics(request.isAllowedInCosmetics());
		ingredient.setSideEffects(request.getSideEffects());
		ingredient.setReferences(request.getReferences());
	}

	private IngredientResponse mapToResponse(Ingredient ingredient) {
		IngredientResponse response = new IngredientResponse();
		response.setId(ingredient.getId());
		response.setName(ingredient.getName());
		response.setScientificName(ingredient.getScientificName());
		response.setRiskLevel(ingredient.getRiskLevel());
		response.setAllowedInCosmetics(ingredient.isAllowedInCosmetics());
		response.setSideEffects(ingredient.getSideEffects());
		response.setReferences(ingredient.getReferences());
		return response;
	}
}
