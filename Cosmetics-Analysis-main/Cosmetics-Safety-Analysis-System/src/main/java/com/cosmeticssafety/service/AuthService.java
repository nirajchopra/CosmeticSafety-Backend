package com.cosmeticssafety.service;

import com.cosmeticssafety.dto.AuthRequest;
import com.cosmeticssafety.dto.AuthResponse;
import com.cosmeticssafety.dto.ChangePasswordRequest;
import com.cosmeticssafety.dto.ForgotPasswordRequest;
import com.cosmeticssafety.dto.MessageResponse;
import com.cosmeticssafety.dto.RegisterRequest;
import com.cosmeticssafety.dto.ResetPasswordRequest;

public interface AuthService {

	AuthResponse register(RegisterRequest request);

	AuthResponse login(AuthRequest request);

	MessageResponse forgotPassword(ForgotPasswordRequest request);

	MessageResponse resetPassword(ResetPasswordRequest request);

	MessageResponse changePassword(String email, ChangePasswordRequest request);
}
