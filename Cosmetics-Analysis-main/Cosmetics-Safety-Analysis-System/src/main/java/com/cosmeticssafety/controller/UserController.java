package com.cosmeticssafety.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmeticssafety.common.constants.AppConstants;
import com.cosmeticssafety.dto.ChangePasswordRequest;
import com.cosmeticssafety.dto.MessageResponse;
import com.cosmeticssafety.dto.RoleAssignmentRequest;
import com.cosmeticssafety.dto.UpdateProfileRequest;
import com.cosmeticssafety.dto.UserAccountStatusUpdateRequest;
import com.cosmeticssafety.dto.UserProfileResponse;
import com.cosmeticssafety.service.UserService;

@RestController
@RequestMapping(AppConstants.API_VERSION_V1 + "/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public UserProfileResponse getMyProfile(Authentication authentication) {
		return userService.getCurrentUserProfile(authentication.getName());
	}

	@PutMapping("/me")
	public UserProfileResponse updateMyProfile(@Valid @RequestBody UpdateProfileRequest request,
			Authentication authentication) {
		return userService.updateCurrentUserProfile(authentication.getName(), request);
	}

	@PutMapping("/me/change-password")
	public MessageResponse changePassword(@Valid @RequestBody ChangePasswordRequest request,
			Authentication authentication) {
		return userService.changePassword(authentication.getName(), request);
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserProfileResponse> getAllUsers() {
		return userService.getAllUsers();
	}

	@PutMapping("/{userId}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public UserProfileResponse updateUserStatus(@PathVariable Long userId,
			@RequestBody UserAccountStatusUpdateRequest request) {
		return userService.updateUserAccountStatus(userId, request);
	}

	@PutMapping("/{userId}/roles")
	@PreAuthorize("hasRole('ADMIN')")
	public UserProfileResponse assignRoles(@PathVariable Long userId,
			@Valid @RequestBody RoleAssignmentRequest request) {
		return userService.assignRoles(userId, request);
	}
}
