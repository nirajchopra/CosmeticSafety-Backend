package com.cosmeticssafety.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmeticssafety.common.constants.AppConstants;
import com.cosmeticssafety.dto.ComplaintCreateRequest;
import com.cosmeticssafety.dto.ComplaintResponse;
import com.cosmeticssafety.dto.ComplaintStatusUpdateRequest;
import com.cosmeticssafety.service.ComplaintService;

@RestController
@RequestMapping(AppConstants.API_VERSION_V1 + "/complaints")
public class ComplaintController {

	private final ComplaintService complaintService;

	public ComplaintController(ComplaintService complaintService) {
		this.complaintService = complaintService;
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('CONSUMER','ADMIN')")
	public ComplaintResponse createComplaint(@Valid @RequestBody ComplaintCreateRequest request,
			Authentication authentication) {
		return complaintService.createComplaint(request, authentication.getName());
	}

	@GetMapping("/my")
	@PreAuthorize("hasAnyRole('CONSUMER','ADMIN','ANALYST')")
	public List<ComplaintResponse> getMyComplaints(Authentication authentication) {
		return complaintService.getComplaintsForCurrentUser(authentication.getName());
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
	public List<ComplaintResponse> getAllComplaints() {
		return complaintService.getAllComplaints();
	}

	@PutMapping("/{complaintId}/status")
	@PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
	public ComplaintResponse updateComplaintStatus(@PathVariable Long complaintId,
			@Valid @RequestBody ComplaintStatusUpdateRequest request) {
		return complaintService.updateComplaintStatus(complaintId, request);
	}
}
