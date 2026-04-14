package com.cosmeticssafety.dto;

import java.util.Set;

import javax.validation.constraints.NotEmpty;

import com.cosmeticssafety.common.enums.RoleType;

public class RoleAssignmentRequest {

	@NotEmpty(message = "roles are required")
	private Set<RoleType> roles;

	public Set<RoleType> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleType> roles) {
		this.roles = roles;
	}
}
