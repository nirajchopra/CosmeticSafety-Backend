package com.cosmeticssafety.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.cosmeticssafety.common.enums.IngredientRiskLevel;

@Entity
@Table(name = "ingredients")
public class Ingredient {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 150)
	private String name;

	@Column(length = 150)
	private String scientificName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private IngredientRiskLevel riskLevel;

	@Column(nullable = false)
	private boolean allowedInCosmetics = true;

	@Column(length = 1000)
	private String sideEffects;

	@Column(name = "reference_info", length = 1500)
	private String references;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	public void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	public void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
