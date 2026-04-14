package com.cosmeticssafety.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cosmeticssafety.common.enums.IngredientRiskLevel;

public class IngredientRequest {

	@NotBlank(message = "name is required")
	private String name;

	private String scientificName;

	@NotNull(message = "riskLevel is required")
	private IngredientRiskLevel riskLevel;

	private boolean allowedInCosmetics = true;
	private String sideEffects;
	private String references;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public IngredientRiskLevel getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(IngredientRiskLevel riskLevel) {
		this.riskLevel = riskLevel;
	}

	public boolean isAllowedInCosmetics() {
		return allowedInCosmetics;
	}

	public void setAllowedInCosmetics(boolean allowedInCosmetics) {
		this.allowedInCosmetics = allowedInCosmetics;
	}

	public String getSideEffects() {
		return sideEffects;
	}

	public void setSideEffects(String sideEffects) {
		this.sideEffects = sideEffects;
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
	}
}
