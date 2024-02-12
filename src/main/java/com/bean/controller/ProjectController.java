package com.bean.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.bean.domain.Status;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Assignment;
import com.bean.model.Wage;
import com.bean.repository.ProjectRepository;
import com.bean.service.InvoiceService;
import com.bean.service.ProjectService;
import com.bean.model.Project;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class ProjectController {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ProjectService projectService;

	@GetMapping("/projects")
	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	@GetMapping("/activeProjects")
	public List<com.bean.domain.Project> getAllActiveProjects(@RequestParam(required = true) String endDate,
			@RequestParam(required = true) String selectedDate) {

		String startDate = "2020-01-01"; // to read from property file

		logger.info("endDate: " + endDate);

		var activeProjects = projectRepository.findAllActiveProjectsByDate(startDate, endDate);
		logger.info(activeProjects.toString());
		List<com.bean.domain.Project> flattenProjects = new ArrayList<>();
		activeProjects.stream().forEach(project -> {
			project.getBillRates().forEach(billrate -> {
				flattenProjects.add(projectService.createProject(project, billrate, selectedDate));
			});
		});

		flattenProjects.forEach(project -> {
			System.out.println(project);
		});
		
		return flattenProjects;
		// return projectRepository.findAll().stream().filter(project ->
		// project.getStartDate().isAfter()after(start) && dates.before(end)
	}

	@GetMapping("/getProjects")
	public List<com.bean.domain.Project> getProjects() {
		var repoProjects = projectRepository.findAll();
		List<com.bean.domain.Project> flattenProjects = new ArrayList<>();
		repoProjects.stream().forEach(project -> {
			project.getBillRates().forEach(billrate -> {
				flattenProjects.add(projectService.createProject(project, billrate, null));
			});

			/*
			 * project.getEmployee().getEmployeeAssignments().forEach(employeeAssignment->{
			 * // if employee assignment falls in the bill rate period create a project //
			 * if billrate started earlier or same time as assignment boolean isInRange =
			 * billrate.getStartDate().isEqual(employeeAssignment.getStartDate()) ||
			 * billrate.getStartDate().isBefore(employeeAssignment.getStartDate()) &&
			 * (billrate.getEndDate().isEqual(employeeAssignment.getEndDate()) ||
			 * billrate.getEndDate().isAfter(employeeAssignment.getEndDate()));
			 * if(isInRange)
			 * flattenProjects.add(createProject(project,billrate,employeeAssignment));});
			 */
			// System.out.println("Suresh");
			// });
		});

		flattenProjects.forEach(project -> {
			System.out.println(project);
		});

		return flattenProjects;
		// return projectRepository.findAll().stream().filter(project ->
		// project.getStartDate().isAfter()after(start) && dates.before(end)
	}

	public com.bean.domain.Project updateBillRate(com.bean.domain.Project project, Assignment assignment) {

		return project;
	}

	@PostMapping("/projects")
	public Project createProject(@RequestBody Project project) {
		return projectRepository.save(project);
	}

	@GetMapping("/projects/{id}")
	public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project not exist with id :" + id));
		return ResponseEntity.ok(project);
	}

	@PutMapping("/projects/{id}")
	public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project not exist with id :" + id));

		project.setProjectName(projectDetails.getProjectName());
		// project.setVendor(projectDetails.getVendor());

		Project updatedProject = projectRepository.save(project);
		return ResponseEntity.ok(updatedProject);
	}

	@DeleteMapping("/projects/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteProject(@PathVariable Long id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project not exist with id :" + id));

		projectRepository.delete(project);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
