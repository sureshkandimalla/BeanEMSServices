package com.bean.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.model.Assignment;
import com.bean.model.Bills;
import com.bean.model.Invoice;
import com.bean.repository.AssignmentRepository;
import com.bean.repository.BillsRepository;

@Service
public class AssignmentService {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AssignmentService.class);
	
	@Autowired
    private AssignmentRepository assignmentRepository;
	
	public Optional<List<Assignment>> findActiveAssignmentsByEndDate(String formattedDate) {
		
		Optional<List<Assignment>> activeAssignments = assignmentRepository.findActiveAssignmentsByEndDate(formattedDate);
		
		// TODO mapping based on UI display
		return activeAssignments;
	}

}
