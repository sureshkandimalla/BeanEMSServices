package com.bean.controller;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bean.repository.AssignmentRepository;
import com.bean.service.AssignmentService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Assignment;
import com.bean.model.Bills;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4000"})
@RestController
@RequestMapping("/api/v1/")
public class AssignmentController {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AssignmentController.class);

    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentService assignmentService;

    // Get all assignments
    @GetMapping("/assignments")
    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }


    @GetMapping("/getActiveAssignments")
    public ResponseEntity<List<Assignment>> getAllActiveAssignments() {
    	
		/*
		 * logger.info("selectedDate:::::"+selectedDate); LocalDate date =
		 * LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		 * String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMM"));
		 */
        
        //get active assignments by enddate, asof now cannot have selecteddate 
        Optional<List<Assignment>> assignList= assignmentService.findActiveAssignmentsByEndDate(LocalDate.now().toString());
        logger.info("billsList:: "+assignList.toString());
    	return assignList.map(assigns -> ResponseEntity.ok(assigns))
                .orElse(null);
    }

    // Create assignment rest api
    @PostMapping("/assignments")
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        LocalDate today = LocalDate.now();
        if (assignment.getStartDate().isBefore(today) && assignment.getEndDate().isAfter(today))
            assignment.setStatus("Active");
        else
            assignment.setStatus("Inactive");
            // The assignment is active
            return assignmentRepository.save(assignment);
    }

    // Get assignment by id rest api
    @GetMapping("/assignments/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@PathVariable Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not exist with id :" + id));
        return ResponseEntity.ok(assignment);
    }

    @GetMapping("/assignmentsForProject")
    public ResponseEntity<List<com.bean.domain.Assignment>> getAssignmentByProjectId(@RequestParam Long projectId) {

        List<com.bean.domain.Assignment> x=assignmentRepository.findAssignmentsForProject(projectId).stream().map(
                result->new com.bean.domain.Assignment(
                (String) result[0],(String) result[1],(BigInteger) result[2],(Float) result[3]
                ,(String)result[4],(String)result[5],(Date)result[6],(Date)result[7],(Date)result[8])
        ).collect(Collectors.toList());;
        System.out.println(x);

      //  var assignment = assignmentRepository.findAssignmentsForProject(projectId)
               // .orElseThrow(() -> new ResourceNotFoundException("Assignment not exist with id :" + projectId));
        return ResponseEntity.ok(x);
    }

    // Update assignment rest api
    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long id, @RequestBody Assignment assignmentDetails) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not exist with id :" + id));

         assignment.setAssignmentType(assignmentDetails.getAssignmentType());
    assignment.setStartDate(assignmentDetails.getStartDate());
    assignment.setEndDate(assignmentDetails.getEndDate());


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
