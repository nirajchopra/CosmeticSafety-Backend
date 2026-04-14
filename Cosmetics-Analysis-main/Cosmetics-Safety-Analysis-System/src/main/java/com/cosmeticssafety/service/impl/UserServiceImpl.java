package com.cosmeticssafety.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmeticssafety.common.enums.RoleType;
import com.cosmeticssafety.dto.ChangePasswordRequest;
import com.cosmeticssafety.dto.MessageResponse;
import com.cosmeticssafety.dto.RoleAssignmentRequest;
import com.cosmeticssafety.dto.UpdateProfileRequest;
import com.cosmeticssafety.dto.UserAccountStatusUpdateRequest;
import com.cosmeticssafety.dto.UserProfileResponse;
import com.cosmeticssafety.entity.Role;
import com.cosmeticssafety.entity.User;
import com.cosmeticssafety.exception.ResourceNotFoundException;
import com.cosmeticssafety.repository.RoleRepository;
import com.cosmeticssafety.repository.UserRepository;
import com.cosmeticssafety.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	@Transactional(readOnly = true)
	public UserProfileResponse getCurrentUserProfile(String email) {
		return mapToResponse(getUserByEmail(email));
	}

	@Override
	@Transactional
	public UserProfileResponse updateCurrentUserProfile(String email, UpdateProfileRequest request) {
		User user = getUserByEmail(email);
		user.setFullName(request.getFullName());
		return mapToResponse(userRepository.save(user));
	}

	@Override
	@Transactional
	public MessageResponse changePassword(String email, ChangePasswordRequest request) {
		User user = getUserByEmail(email);
		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new BadCredentialsException("Current password is incorrect");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		return new MessageResponse("Password changed successfully");
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserProfileResponse> getAllUsers() {
		return userRepository.findAll().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public UserProfileResponse updateUserAccountStatus(Long userId, UserAccountStatusUpdateRequest request) {
		User user = getUserById(userId);
		user.setEnabled(request.isEnabled());
		user.setAccountLocked(request.isAccountLocked());
		return mapToResponse(userRepository.save(user));
	}

	@Override
	@Transactional
	public UserProfileResponse assignRoles(Long userId, RoleAssignmentRequest request) {
		User user = getUserById(userId);
		Set<Role> roles = request.getRoles().stream()
				.map(this::getRoleByType)
				.collect(Collectors.toSet());
		user.setRoles(roles);
		return mapToResponse(userRepository.save(user));
	}

	private User getUserByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	}

	private Role getRoleByType(RoleType roleType) {
		return roleRepository.findByName(roleType)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleType));
	}

	private UserProfileResponse mapToResponse(User user) {
		UserProfileResponse response = new UserProfileResponse();
		response.setId(user.getId());
		response.setFullName(user.getFullName());
		response.setEmail(user.getEmail());
		response.setEnabled(user.isEnabled());
		response.setAccountLocked(user.isAccountLocked());
		response.setCreatedAt(user.getCreatedAt());
		response.setRoles(user.getRoles().stream()
				.map(role -> role.getName().name())
				.collect(Collectors.toList()));
		return response;
	}
}
