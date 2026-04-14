package com.cosmeticssafety.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cosmeticssafety.common.enums.RoleType;
import com.cosmeticssafety.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(RoleType name);
}
