package com.cosmeticssafety.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmeticssafety.common.constants.AppConstants;
import com.cosmeticssafety.dto.IngredientRequest;
import com.cosmeticssafety.dto.IngredientResponse;
import com.cosmeticssafety.service.IngredientService;

@RestController
@RequestMapping(AppConstants.API_VERSION_V1 + "/ingredients")
public class IngredientController {

	private final IngredientService ingredientService;

	public IngredientController(IngredientService ingredientService) {
		this.ingredientService = ingredientService;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
	public IngredientResponse createIngredient(@Valid @RequestBody IngredientRequest request) {
		return ingredientService.createIngredient(request);
	}

	@PutMapping("/{ingredientId}")
	@PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
	public IngredientResponse updateIngredient(@PathVariable Long ingredientId,
			@Valid @RequestBody IngredientRequest request) {
		return ingredientService.updateIngredient(ingredientId, request);
	}

	@GetMapping
	public List<IngredientResponse> getAllIngredients() {
		return ingredientService.getAllIngredients();
	}
}
