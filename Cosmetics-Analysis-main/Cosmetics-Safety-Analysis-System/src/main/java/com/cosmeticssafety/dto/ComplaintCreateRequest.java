package com.cosmeticssafety.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ComplaintCreateRequest {

	@NotBlank(message = "productName is required")
	private String productName;

	@NotBlank(message = "brandName is required")
	private String brandName;

	@NotBlank(message = "issueType is required")
	private String issueType;

	@NotBlank(message = "description is required")
	@Size(min = 10, message = "must be at least 10 characters")
	private String description;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
