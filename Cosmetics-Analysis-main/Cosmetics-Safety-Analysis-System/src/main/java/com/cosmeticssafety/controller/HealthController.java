package com.cosmeticssafety.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmeticssafety.common.constants.AppConstants;

@RestController
@RequestMapping(AppConstants.API_VERSION_V1)
public class HealthController {

	@GetMapping("/health")
	public ResponseEntity<Map<String, String>> health() {
		Map<String, String> response = new LinkedHashMap<>();
		response.put("application", "Cosmetics Safety Analysis System");
		response.put("status", "UP");
		response.put("version", "v1");
		return ResponseEntity.ok(response);
	}
}
