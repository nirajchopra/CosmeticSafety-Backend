package com.cosmeticssafety.config;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cosmeticssafety.common.enums.RoleType;
import com.cosmeticssafety.entity.Role;
import com.cosmeticssafety.entity.User;
import com.cosmeticssafety.repository.RoleRepository;
import com.cosmeticssafety.repository.UserRepository;

@Configuration
public class DataInitializer {

	@Bean
	public CommandLineRunner initializeSecurityData(RoleRepository roleRepository, UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			Map<RoleType, String[]> defaultUsers = new LinkedHashMap<>();
			defaultUsers.put(RoleType.ADMIN, new String[] { "System Admin", "admin@cosmeticssafety.com" });
			defaultUsers.put(RoleType.ANALYST, new String[] { "Safety Analyst", "analyst@cosmeticssafety.com" });
			defaultUsers.put(RoleType.CONSUMER, new String[] { "Default Consumer", "consumer@cosmeticssafety.com" });

			for (RoleType roleType : RoleType.values()) {
				Role role = roleRepository.findByName(roleType)
						.orElseGet(() -> roleRepository.save(new Role(roleType)));

				String[] userData = defaultUsers.get(roleType);
				if (!userRepository.existsByEmailIgnoreCase(userData[1])) {
					User user = new User();
					user.setFullName(userData[0]);
					user.setEmail(userData[1]);
					user.setPassword(passwordEncoder.encode("Password@123"));
					user.setRoles(Collections.singleton(role));
					userRepository.save(user);
				}
			}
		};
	}
}
