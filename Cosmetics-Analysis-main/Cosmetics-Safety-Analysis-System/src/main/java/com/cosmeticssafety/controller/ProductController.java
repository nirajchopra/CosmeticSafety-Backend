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
import com.cosmeticssafety.dto.ProductRequest;
import com.cosmeticssafety.dto.ProductResponse;
import com.cosmeticssafety.service.ProductService;

@RestController
@RequestMapping(AppConstants.API_VERSION_V1 + "/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
	public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
		return productService.createProduct(request);
	}

	@PutMapping("/{productId}")
	@PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
	public ProductResponse updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequest request) {
		return productService.updateProduct(productId, request);
	}

	@GetMapping
	public List<ProductResponse> getAllProducts() {
		return productService.getAllProducts();
	}
}
