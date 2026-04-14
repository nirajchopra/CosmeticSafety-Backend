package com.cosmeticssafety.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmeticssafety.common.constants.AppConstants;
import com.cosmeticssafety.dto.BrandRequest;
import com.cosmeticssafety.dto.BrandResponse;
import com.cosmeticssafety.service.BrandService;

@RestController
@RequestMapping(AppConstants.API_VERSION_V1 + "/brands")
public class BrandController {

	private final BrandService brandService;

	public BrandController(BrandService brandService) {
		this.brandService = brandService;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
	public BrandResponse createBrand(@Valid @RequestBody BrandRequest request) {
		return brandService.createBrand(request);
	}

	@PutMapping("/{brandId}")
	@PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
	public BrandResponse updateBrand(@PathVariable Long brandId, @Valid @RequestBody BrandRequest request) {
		return brandService.updateBrand(brandId, request);
	}

	@GetMapping
	public List<BrandResponse> getAllBrands() {
		return brandService.getAllBrands();
	}
}
