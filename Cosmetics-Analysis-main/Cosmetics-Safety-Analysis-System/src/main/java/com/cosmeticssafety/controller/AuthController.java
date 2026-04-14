package com.cosmeticssafety.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmeticssafety.common.constants.AppConstants;
import com.cosmeticssafety.dto.AuthRequest;
import com.cosmeticssafety.dto.AuthResponse;
import com.cosmeticssafety.dto.ChangePasswordRequest;
import com.cosmeticssafety.dto.ForgotPasswordRequest;
import com.cosmeticssafety.dto.MessageResponse;
import com.cosmeticssafety.dto.RegisterRequest;
import com.cosmeticssafety.dto.ResetPasswordRequest;
import com.cosmeticssafety.service.AuthService;

@RestController
@RequestMapping(AppConstants.API_VERSION_V1 + "/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		return ResponseEntity.ok(authService.forgotPassword(request));
	}

	@PostMapping("/reset-password")
	public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		return ResponseEntity.ok(authService.resetPassword(request));
	}

	@PutMapping("/change-password")
	public ResponseEntity<MessageResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request,
			Authentication authentication) {
		return ResponseEntity.ok(authService.changePassword(authentication.getName(), request));
	}
}
