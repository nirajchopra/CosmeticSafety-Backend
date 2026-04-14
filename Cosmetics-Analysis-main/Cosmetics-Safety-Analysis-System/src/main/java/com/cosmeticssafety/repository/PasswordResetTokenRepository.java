package com.cosmeticssafety.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cosmeticssafety.entity.PasswordResetToken;
import com.cosmeticssafety.entity.User;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	Optional<PasswordResetToken> findByToken(String token);

	void deleteByUser(User user);
}
