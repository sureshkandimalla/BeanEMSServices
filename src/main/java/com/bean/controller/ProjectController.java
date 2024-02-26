package com.bean.controller;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.bean.domain.Dashboard;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Assignment;
import com.bean.model.Wage;
import com.bean.repository.AssignmentRepository;
import com.bean.repository.ProjectRepository;
import com.bean.service.InvoiceService;
import com.bean.service.ProjectService;
import com.bean.model.Project;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class ProjectController {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private AssignmentRepository assignmentRepository;

	/*
	 * @GetMapping("/projects") public List<Project> getAllProjects() { return
	 * projectRepository.findAll(); }
	 */

	@GetMapping("/activeProjects")
	public List<com.bean.domain.Project> getAllActiveProjects(@RequestParam(required = true) String endDate,
			@RequestParam(required = true) String selectedDate) {

		String startDate = "2020-01-01"; // to read from property file

		logger.info("endDate: " + endDate);

		var activeProjects = projectRepository.findAllActiveProjectsByDate(endDate);
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
	}


	@GetMapping("/projectDashboard")
	public com.bean.domain.Dashboard projectDashboard() {


		var activeProjects = projectRepository.findAllActiveProjectsByDate(LocalDate.now().toString());
		logger.info(null, activeProjects.size());
		com.bean.domain.Dashboard dashboardData = new Dashboard();
		
		double totalWageBillRate = activeProjects.stream()
			    .flatMap(project -> project.getBillRates().stream())
			    .mapToDouble(billRate -> billRate.getWage())
			    .sum();

		// below form assignments table based on active projectId
		
		//var activeAssignments = assignmentRepository.findActiveAssignmentsByEndDate(LocalDate.now().toString());
		//double totalAssignWage = activeAssignments.stream().mapToDouble(billRate -> billRate.getWage()).sum();
		
		List<Long> projectIds = activeProjects.stream().map(project -> project.getProjectId())
				.collect(Collectors.toList());

		// Fetch sum of wages from assignments table
		double totalWageFromAssignments = assignmentRepository.getTotalWageByProjectIds(projectIds);
		
		Optional.ofNullable(activeProjects.size())
        .ifPresentOrElse(
                size -> dashboardData.setActiveProjects(size),
                () -> dashboardData.setActiveProjects(0)
        );
		Optional.ofNullable(totalWageBillRate).ifPresentOrElse(wage -> dashboardData.setTotalRevenue(wage),() -> dashboardData.setTotalRevenue(0));
		Optional.ofNullable(totalWageFromAssignments).ifPresentOrElse(wage -> dashboardData.setTotalCost(wage),() -> dashboardData.setTotalCost(0));
		logger.info("dashboardData:: "+dashboardData.toString());
		
		return dashboardData;
	}


	@GetMapping("/getProjects")
	public List<com.bean.domain.Project> getProjects() {
		var repoProjects = projectRepository.findAll();
		List<com.bean.domain.Project> flattenProjects = new ArrayList<>();
		repoProjects.stream().forEach(project -> {
			project.getBillRates().forEach(billrate -> {
				flattenProjects.add(projectService.createProject(project, billrate, null));
			});
		});

		flattenProjects.forEach(project -> {
			System.out.println(project);
		});

		return flattenProjects;
	}

	public com.bean.domain.Project updateBillRate(com.bean.domain.Project project, Assignment assignment) {

		return project;
	}

	@PostMapping("/projects/saveOnBoardProject")
	public ResponseEntity<String> createProject(@RequestBody com.bean.domain.Project project) {
		
		ResponseEntity<String> savedProject = projectService.saveProject(project);
		return savedProject;
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
