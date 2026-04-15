package com.cosmeticssafety.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmeticssafety.dto.ProductRequest;
import com.cosmeticssafety.dto.ProductResponse;
import com.cosmeticssafety.entity.Brand;
import com.cosmeticssafety.entity.Product;
import com.cosmeticssafety.exception.ResourceNotFoundException;
import com.cosmeticssafety.repository.BrandRepository;
import com.cosmeticssafety.repository.ProductRepository;
import com.cosmeticssafety.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

	private final ProductRepository productRepository;
	private final BrandRepository brandRepository;

	public ProductServiceImpl(ProductRepository productRepository, BrandRepository brandRepository) {
		this.productRepository = productRepository;
		this.brandRepository = brandRepository;
	}

	@Override
	@Transactional
	public ProductResponse createProduct(ProductRequest request) {
		Product product = new Product();
		applyProductValues(product, request);
		Product savedProduct = productRepository.saveAndFlush(product);
		LOGGER.info("Created product with id={} and name={}", savedProduct.getId(), savedProduct.getName());
		return mapToResponse(savedProduct);
	}

	@Override
	@Transactional
	public ProductResponse updateProduct(Long productId, ProductRequest request) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
		applyProductValues(product, request);
		Product savedProduct = productRepository.saveAndFlush(product);
		LOGGER.info("Updated product with id={}", savedProduct.getId());
		return mapToResponse(savedProduct);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProductResponse> getAllProducts() {
		return productRepository.findAllByOrderByCreatedAtDesc().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	private void applyProductValues(Product product, ProductRequest request) {
		Brand brand = brandRepository.findById(request.getBrandId())
				.orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + request.getBrandId()));
		product.setName(request.getName());
		product.setProductType(request.getProductType());
		product.setSkuCode(request.getSkuCode());
		product.setDescription(request.getDescription());
		product.setActive(request.isActive());
		product.setBrand(brand);
	}

	private ProductResponse mapToResponse(Product product) {
		ProductResponse response = new ProductResponse();
		response.setId(product.getId());
		response.setName(product.getName());
		response.setProductType(product.getProductType());
		response.setSkuCode(product.getSkuCode());
		response.setDescription(product.getDescription());
		response.setActive(product.isActive());
		response.setBrandId(product.getBrand().getId());
		response.setBrandName(product.getBrand().getName());
		return response;
	}
}
