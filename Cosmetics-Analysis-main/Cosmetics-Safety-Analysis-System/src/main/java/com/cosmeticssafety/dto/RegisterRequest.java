package com.cosmeticssafety.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequest {

	@NotBlank(message = "fullName is required")
	private String fullName;

	@NotBlank(message = "email is required")
	@Email(message = "must be a valid email")
	private String email;

	@NotBlank(message = "password is required")
	@Size(min = 8, message = "must be at least 8 characters")
	private String password;

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
