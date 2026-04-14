package com.cosmeticssafety.dto;

import javax.validation.constraints.NotBlank;

public class UpdateProfileRequest {

	@NotBlank(message = "fullName is required")
	private String fullName;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
