package com.cosmeticssafety.service;

import java.util.List;

import com.cosmeticssafety.dto.ProductRequest;
import com.cosmeticssafety.dto.ProductResponse;

public interface ProductService {

	ProductResponse createProduct(ProductRequest request);

	ProductResponse updateProduct(Long productId, ProductRequest request);

	List<ProductResponse> getAllProducts();
}
