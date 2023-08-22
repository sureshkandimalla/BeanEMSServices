package com.bean.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bean.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Assignment;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepository;

    // Get all assignments
    @GetMapping("/assignments")
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }


    @GetMapping("/activeAssignments")
    public List<Assignment> getAllActiveAssignments(String startDate,String endDate) {
       // LocalDate today = LocalDate.now();
        if(endDate==null || endDate.length()<6)
            endDate=startDate;
        return assignmentRepository.findAllActiveAssignment(startDate,endDate);/*findAll().stream().filter(a->{
            if(a.getStartDate().isBefore(today) && a.getEndDate().isAfter(today))
                return true;
            else
                return false;
        }).collect(Collectors.toList());*/
    }

    // Create assignment rest api
    @PostMapping("/assignments")
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    // Get assignment by id rest api
    @GetMapping("/assignments/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not exist with id :" + id));
        return ResponseEntity.ok(assignment);
    }

    // Update assignment rest api
    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long id, @RequestBody Assignment assignmentDetails) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not exist with id :" + id));

         assignment.setAssignmentType(assignmentDetails.getAssignmentType());
    assignment.setStartDate(assignmentDetails.getStartDate());
    assignment.setEndDate(assignmentDetails.getEndDate());
     assignment.setBillRate(assignmentDetails.getBillRate());
    assignment.setStatus(assignmentDetails.getStatus());
    assignment.setNote(assignmentDetails.getNote());

        Assignment updatedAssignment = assignmentRepository.save(assignment);
        return ResponseEntity.ok(updatedAssignment);
    }

    // Delete assignment rest api
    @DeleteMapping("/assignments/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteAssignment(@PathVariable Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not exist with id :" + id));

        assignmentRepository.delete(assignment);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
