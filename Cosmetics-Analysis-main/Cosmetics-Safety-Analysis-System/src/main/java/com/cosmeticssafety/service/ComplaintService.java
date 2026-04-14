package com.cosmeticssafety.service;

import java.util.List;

import com.cosmeticssafety.dto.ComplaintCreateRequest;
import com.cosmeticssafety.dto.ComplaintResponse;
import com.cosmeticssafety.dto.ComplaintStatusUpdateRequest;

public interface ComplaintService {

	ComplaintResponse createComplaint(ComplaintCreateRequest request, String userEmail);

	List<ComplaintResponse> getComplaintsForCurrentUser(String userEmail);

	List<ComplaintResponse> getAllComplaints();

	ComplaintResponse updateComplaintStatus(Long complaintId, ComplaintStatusUpdateRequest request);
}
