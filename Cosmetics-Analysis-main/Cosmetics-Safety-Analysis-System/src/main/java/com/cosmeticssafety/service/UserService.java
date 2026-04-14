package com.cosmeticssafety.service;

import java.util.List;

import com.cosmeticssafety.dto.ChangePasswordRequest;
import com.cosmeticssafety.dto.MessageResponse;
import com.cosmeticssafety.dto.RoleAssignmentRequest;
import com.cosmeticssafety.dto.UpdateProfileRequest;
import com.cosmeticssafety.dto.UserAccountStatusUpdateRequest;
import com.cosmeticssafety.dto.UserProfileResponse;

public interface UserService {

	UserProfileResponse getCurrentUserProfile(String email);

	UserProfileResponse updateCurrentUserProfile(String email, UpdateProfileRequest request);

	MessageResponse changePassword(String email, ChangePasswordRequest request);

	List<UserProfileResponse> getAllUsers();

	UserProfileResponse updateUserAccountStatus(Long userId, UserAccountStatusUpdateRequest request);

	UserProfileResponse assignRoles(Long userId, RoleAssignmentRequest request);
}
