package com.cosmeticssafety.service;

import java.util.List;

import com.cosmeticssafety.dto.BrandRequest;
import com.cosmeticssafety.dto.BrandResponse;

public interface BrandService {

	BrandResponse createBrand(BrandRequest request);

	BrandResponse updateBrand(Long brandId, BrandRequest request);

	List<BrandResponse> getAllBrands();
}
