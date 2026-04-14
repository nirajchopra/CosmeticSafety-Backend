package com.cosmeticssafety.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cosmeticssafety.entity.Complaint;
import com.cosmeticssafety.entity.User;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

	List<Complaint> findAllBySubmittedByOrderByCreatedAtDesc(User submittedBy);

	List<Complaint> findAllByOrderByCreatedAtDesc();
}
