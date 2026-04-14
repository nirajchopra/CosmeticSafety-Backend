package com.cosmeticssafety.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cosmeticssafety.entity.Complaint;
import com.cosmeticssafety.entity.User;
import com.cosmeticssafety.dto.ComplaintCreateRequest;
import com.cosmeticssafety.dto.ComplaintResponse;
import com.cosmeticssafety.dto.ComplaintStatusUpdateRequest;
import com.cosmeticssafety.exception.ResourceNotFoundException;
import com.cosmeticssafety.repository.ComplaintRepository;
import com.cosmeticssafety.repository.UserRepository;
import com.cosmeticssafety.service.ComplaintService;

@Service
public class ComplaintServiceImpl implements ComplaintService {

	private final ComplaintRepository complaintRepository;
	private final UserRepository userRepository;

	public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository) {
		this.complaintRepository = complaintRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public ComplaintResponse createComplaint(ComplaintCreateRequest request, String userEmail) {
		User user = getUserByEmail(userEmail);

		Complaint complaint = new Complaint();
		complaint.setProductName(request.getProductName());
		complaint.setBrandName(request.getBrandName());
		complaint.setIssueType(request.getIssueType());
		complaint.setDescription(request.getDescription());
		complaint.setSubmittedBy(user);

		return mapToResponse(complaintRepository.save(complaint));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComplaintResponse> getComplaintsForCurrentUser(String userEmail) {
		User user = getUserByEmail(userEmail);
		return complaintRepository.findAllBySubmittedByOrderByCreatedAtDesc(user).stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ComplaintResponse> getAllComplaints() {
		return complaintRepository.findAllByOrderByCreatedAtDesc().stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ComplaintResponse updateComplaintStatus(Long complaintId, ComplaintStatusUpdateRequest request) {
		Complaint complaint = complaintRepository.findById(complaintId)
				.orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id: " + complaintId));

		complaint.setStatus(request.getStatus());
		complaint.setResolutionRemarks(request.getResolutionRemarks());
		return mapToResponse(complaintRepository.save(complaint));
	}

	private User getUserByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
	}

	private ComplaintResponse mapToResponse(Complaint complaint) {
		ComplaintResponse response = new ComplaintResponse();
		response.setId(complaint.getId());
		response.setProductName(complaint.getProductName());
		response.setBrandName(complaint.getBrandName());
		response.setIssueType(complaint.getIssueType());
		response.setDescription(complaint.getDescription());
		response.setStatus(complaint.getStatus());
		response.setResolutionRemarks(complaint.getResolutionRemarks());
		response.setCreatedAt(complaint.getCreatedAt());
		response.setSubmittedByName(complaint.getSubmittedBy().getFullName());
		response.setSubmittedByEmail(complaint.getSubmittedBy().getEmail());
		return response;
	}
}
