package com.cosmeticssafety.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cosmeticssafety.common.enums.ComplaintStatus;

public class ComplaintStatusUpdateRequest {

	@NotNull(message = "status is required")
	private ComplaintStatus status;

	@NotBlank(message = "resolutionRemarks is required")
	private String resolutionRemarks;

	public ComplaintStatus getStatus() {
		return status;
	}

	public void setStatus(ComplaintStatus status) {
		this.status = status;
	}

	public String getResolutionRemarks() {
		return resolutionRemarks;
	}

	public void setResolutionRemarks(String resolutionRemarks) {
		this.resolutionRemarks = resolutionRemarks;
	}
}
