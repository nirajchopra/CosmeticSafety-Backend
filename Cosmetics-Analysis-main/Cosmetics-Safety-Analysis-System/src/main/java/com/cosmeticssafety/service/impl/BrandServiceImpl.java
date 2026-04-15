package com.cosmeticssafety.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmeticssafety.dto.BrandRequest;
import com.cosmeticssafety.dto.BrandResponse;
import com.cosmeticssafety.entity.Brand;
import com.cosmeticssafety.exception.ResourceNotFoundException;
import com.cosmeticssafety.repository.BrandRepository;
import com.cosmeticssafety.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BrandServiceImpl.class);

	private final BrandRepository brandRepository;

	public BrandServiceImpl(BrandRepository brandRepository) {
		this.brandRepository = brandRepository;
	}

	@Override
	@Transactional
	public BrandResponse createBrand(BrandRequest request) {
		brandRepository.findByNameIgnoreCase(request.getName()).ifPresent(existing -> {
			LOGGER.warn("Brand creation blocked because name already exists: {}", request.getName());
			throw new IllegalArgumentException("Brand already exists with name: " + request.getName());
		});
		Brand brand = new Brand();
		applyBrandValues(brand, request);
		Brand savedBrand = brandRepository.saveAndFlush(brand);
		LOGGER.info("Created brand with id={} and name={}", savedBrand.getId(), savedBrand.getName());
		return mapToResponse(savedBrand);
	}

	@Override
	@Transactional
	public BrandResponse updateBrand(Long brandId, BrandRequest request) {
		Brand brand = brandRepository.findById(brandId)
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + brandId));
		applyBrandValues(brand, request);
		Brand savedBrand = brandRepository.saveAndFlush(brand);
		LOGGER.info("Updated brand with id={}", savedBrand.getId());
		return mapToResponse(savedBrand);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BrandResponse> getAllBrands() {
		return brandRepository.findAll().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	private void applyBrandValues(Brand brand, BrandRequest request) {
		brand.setName(request.getName());
		brand.setDescription(request.getDescription());
		brand.setActive(request.isActive());
	}

	private BrandResponse mapToResponse(Brand brand) {
		BrandResponse response = new BrandResponse();
		response.setId(brand.getId());
		response.setName(brand.getName());
		response.setDescription(brand.getDescription());
		response.setActive(brand.isActive());
		return response;
	}
}
