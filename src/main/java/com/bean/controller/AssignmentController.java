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

@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com","http://localhost:3000", "http://localhost:4000"})
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
        if (assignment.getEndDate() == null) {
            assignment.setEndDate(LocalDate.of(9999, 12, 31));
        }
        if (assignment.getEndDate().isAfter(LocalDate.now()))
            assignment.setStatus("Active");
        else
            assignment.setStatus("Inactive");
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

        List<com.bean.domain.Assignment> x=assignmentRepository.findAssignmentsForProject(projectId).stream().map(result->new com.bean.domain.Assignment(
                (String) result[0],(String) result[1],(BigInteger) result[2],(Float) result[3]
                ,(String)result[4],(String)result[5],(Date)result[6],(Date)result[7],(Date)result[8])
        ).collect(Collectors.toList());;
        System.out.println(x);

      //  var assignment = assignmentRepository.findAssignmentsForProject(projectId)
               // .orElseThrow(() -> new ResourceNotFoundException("Assignment not exist with id :" + projectId));
        return ResponseEntity.ok(x);
    }

    // Projects (+ assignment) an employee is on, for pickers like the
    // Timesheet page ("which project am I logging hours against?").
    @GetMapping("/assignmentsForEmployee")
    public ResponseEntity<List<Map<String, Object>>> getAssignmentsForEmployee(@RequestParam Long employeeId) {
        List<Map<String, Object>> rows = assignmentRepository.findAssignmentsForEmployee(employeeId).stream()
                .map(result -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("projectId", result[0]);
                    row.put("projectName", result[1]);
                    row.put("assignmentId", result[2]);
                    row.put("wage", result[3]);
                    row.put("assignmentType", result[4]);
                    row.put("status", result[5]);
                    row.put("startDate", result[6]);
                    row.put("endDate", result[7]);
                    return row;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(rows);
    }

    // Every active assignment across all employees/projects, for the
    // Monthly Timesheets bulk-entry page (one row per assignment).
    @GetMapping("/activeAssignmentsWithDetails")
    public ResponseEntity<List<Map<String, Object>>> getActiveAssignmentsWithDetails() {
        List<Map<String, Object>> rows = assignmentRepository.findAllActiveAssignmentsWithDetails().stream()
                .map(result -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("employeeName", result[0] + " " + result[1]);
                    row.put("projectId", result[2]);
                    row.put("projectName", result[3]);
                    row.put("vendorName", result[4]);
                    row.put("assignmentId", result[5]);
                    row.put("employeeId", result[6]);
                    row.put("wage", result[7]);
                    row.put("status", result[8]);
                    row.put("startDate", result[9]);
                    row.put("endDate", result[10]);
                    return row;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(rows);
    }

    // Update assignment rest api. Partial update: the Assignments grid on
    // the Project page only round-trips assignmentId/wage/status/dates —
    // it never has projectId/employeeId/assignmentTaxType/description in
    // its row data. Blindly overwriting every field (the old behavior)
    // would null those out on every edit, so each field is only applied
    // when actually present in the request body.
    @PutMapping("/assignments/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Long id, @RequestBody Assignment assignmentDetails) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not exist with id :" + id));

        if (assignmentDetails.getProjectId() != null) assignment.setProjectId(assignmentDetails.getProjectId());
        if (assignmentDetails.getAssignmentType() != null) assignment.setAssignmentType(assignmentDetails.getAssignmentType());
        if (assignmentDetails.getAssignmentTaxType() != null) assignment.setAssignmentTaxType(assignmentDetails.getAssignmentTaxType());
        if (assignmentDetails.getStartDate() != null) assignment.setStartDate(assignmentDetails.getStartDate());
        if (assignmentDetails.getEndDate() != null) assignment.setEndDate(assignmentDetails.getEndDate());
        if (assignmentDetails.getWage() != 0) assignment.setWage(assignmentDetails.getWage());
        if (assignmentDetails.getEmployeeId() != 0) assignment.setEmployeeId(assignmentDetails.getEmployeeId());
        if (assignmentDetails.getDescription() != null) assignment.setDescription(assignmentDetails.getDescription());

        // An explicit status from the grid always wins; only fall back to
        // computing it from endDate when the caller didn't set one.
        if (assignmentDetails.getStatus() != null && !assignmentDetails.getStatus().isBlank()) {
            assignment.setStatus(assignmentDetails.getStatus());
        } else if (assignmentDetails.getEndDate() != null) {
            assignment.setStatus(assignmentDetails.getEndDate().isAfter(java.time.LocalDate.now()) ? "Active" : "Inactive");
        }

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
