package com.cosmeticssafety.dto;

import com.cosmeticssafety.common.enums.IngredientRiskLevel;

public class IngredientResponse {

	private Long id;
	private String name;
	private String scientificName;
	private IngredientRiskLevel riskLevel;
	private boolean allowedInCosmetics;
	private String sideEffects;
	private String references;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
