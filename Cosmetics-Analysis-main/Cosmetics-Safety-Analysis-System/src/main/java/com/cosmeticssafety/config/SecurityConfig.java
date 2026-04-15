package com.cosmeticssafety.config;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cosmeticssafety.security.CustomUserDetailsService;
import com.cosmeticssafety.security.JwtAuthenticationFilter;
import com.cosmeticssafety.exception.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomUserDetailsService userDetailsService;
	private final ObjectMapper objectMapper;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailsService userDetailsService,
			ObjectMapper objectMapper) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.userDetailsService = userDetailsService;
		this.objectMapper = objectMapper;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.cors().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling()
				.authenticationEntryPoint((request, response, exception) -> writeErrorResponse(response,
						HttpServletResponse.SC_UNAUTHORIZED, "Authentication required", request.getRequestURI()))
				.accessDeniedHandler((request, response, exception) -> writeErrorResponse(response,
						HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource",
						request.getRequestURI()))
				.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/v1/auth/**", "/api/v1/auth/**").permitAll()
				.antMatchers(HttpMethod.GET, "/v1/health", "/api/v1/health").permitAll()
				.antMatchers("/actuator/health", "/actuator/info", "/error").permitAll()
				.anyRequest().authenticated()
				.and()
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.headers().frameOptions().disable();
		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private void writeErrorResponse(HttpServletResponse response, int status, String message, String path)
			throws IOException {
		response.setStatus(status);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ApiErrorResponse errorResponse = new ApiErrorResponse(LocalDateTime.now(), status,
				HttpServletResponse.SC_UNAUTHORIZED == status ? "Unauthorized" : "Forbidden", message, path);
		objectMapper.writeValue(response.getWriter(), errorResponse);
	}
}
