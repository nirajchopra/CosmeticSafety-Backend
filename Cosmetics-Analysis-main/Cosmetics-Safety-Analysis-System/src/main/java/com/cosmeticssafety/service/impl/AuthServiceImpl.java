package com.cosmeticssafety.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmeticssafety.dto.AuthRequest;
import com.cosmeticssafety.dto.AuthResponse;
import com.cosmeticssafety.dto.ChangePasswordRequest;
import com.cosmeticssafety.dto.ForgotPasswordRequest;
import com.cosmeticssafety.dto.MessageResponse;
import com.cosmeticssafety.dto.RegisterRequest;
import com.cosmeticssafety.dto.ResetPasswordRequest;
import com.cosmeticssafety.entity.PasswordResetToken;
import com.cosmeticssafety.entity.Role;
import com.cosmeticssafety.entity.User;
import com.cosmeticssafety.exception.InvalidTokenException;
import com.cosmeticssafety.exception.ResourceNotFoundException;
import com.cosmeticssafety.repository.PasswordResetTokenRepository;
import com.cosmeticssafety.repository.RoleRepository;
import com.cosmeticssafety.repository.UserRepository;
import com.cosmeticssafety.security.ApplicationUserDetails;
import com.cosmeticssafety.security.JwtService;
import com.cosmeticssafety.service.AuthService;
import com.cosmeticssafety.common.enums.RoleType;

@Service
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository,
			RoleRepository roleRepository, PasswordResetTokenRepository passwordResetTokenRepository,
			PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional
	public AuthResponse register(RegisterRequest request) {
		if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
			throw new IllegalArgumentException("User already exists with email: " + request.getEmail());
		}

		Role consumerRole = roleRepository.findByName(RoleType.CONSUMER)
				.orElseThrow(() -> new ResourceNotFoundException("Default CONSUMER role not found"));

		User user = new User();
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.getRoles().add(consumerRole);

		User savedUser = userRepository.save(user);
		return buildAuthResponse(new ApplicationUserDetails(savedUser));
	}

	@Override
	public AuthResponse login(AuthRequest request) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
			ApplicationUserDetails userDetails = (ApplicationUserDetails) authentication.getPrincipal();
			return buildAuthResponse(userDetails);
		} catch (BadCredentialsException exception) {
			throw new BadCredentialsException("Invalid email or password");
		}
	}

	@Override
	@Transactional
	public MessageResponse forgotPassword(ForgotPasswordRequest request) {
		User user = userRepository.findByEmailIgnoreCase(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

		passwordResetTokenRepository.deleteByUser(user);

		String token = UUID.randomUUID().toString().replace("-", "");
		PasswordResetToken resetToken = new PasswordResetToken();
		resetToken.setToken(token);
		resetToken.setUser(user);
		resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
		passwordResetTokenRepository.save(resetToken);

		// For now token is returned for development/testing until email integration is wired.
		return new MessageResponse("Password reset token generated successfully", token);
	}

	@Override
	@Transactional
	public MessageResponse resetPassword(ResetPasswordRequest request) {
		PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
				.orElseThrow(() -> new InvalidTokenException("Invalid password reset token"));

		if (resetToken.isUsed() || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new InvalidTokenException("Password reset token is expired or already used");
		}

		User user = resetToken.getUser();
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);

		resetToken.setUsed(true);
		passwordResetTokenRepository.save(resetToken);

		return new MessageResponse("Password reset successfully");
	}

	@Override
	@Transactional
	public MessageResponse changePassword(String email, ChangePasswordRequest request) {
		User user = userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new BadCredentialsException("Current password is incorrect");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		return new MessageResponse("Password changed successfully");
	}

	private AuthResponse buildAuthResponse(ApplicationUserDetails userDetails) {
		User user = userDetails.getUser();
		List<String> roles = user.getRoles().stream()
				.map(role -> role.getName().name())
				.collect(Collectors.toList());

		HashMap<String, Object> claims = new HashMap<>();
		claims.put("roles", roles);
		claims.put("userId", user.getId());

		String token = jwtService.generateToken(userDetails, claims);

		AuthResponse response = new AuthResponse();
		response.setAccessToken(token);
		response.setTokenType("Bearer");
		response.setUserId(user.getId());
		response.setFullName(user.getFullName());
		response.setEmail(user.getEmail());
		response.setRoles(roles);
		return response;
	}
}
