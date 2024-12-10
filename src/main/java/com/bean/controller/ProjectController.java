package com.bean.controller;

import com.bean.domain.Dashboard;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Assignment;
import com.bean.model.Project;
import com.bean.model.Wage;
import com.bean.repository.AssignmentRepository;
import com.bean.repository.ProjectRepository;
import com.bean.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3001", "http://localhost:4000"})
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


	@GetMapping("/activeProjects")
	public List<com.bean.domain.Project> getAllActiveProjects(@RequestParam(required = true) String endDate,
			@RequestParam(required = true) String selectedDate) {

		logger.info("endDate: " + endDate);
		String selectedMonth=selectedDate.substring(0,7);

		var activeProjects = projectRepository.findAllActiveProjectsForMonth(selectedMonth);
		logger.info(activeProjects.toString());
		List<com.bean.domain.Project> flattenProjects = new ArrayList<>();
		for (Project project : activeProjects)
			project.getBillRates().forEach(billrate -> {
				flattenProjects.add(projectService.createProject(project, billrate, selectedDate));
			});


		  flattenProjects.forEach(project -> { System.out.println(project); });

		
		return flattenProjects;
	}

	@GetMapping("/activeProjectsForInvoiceByEmployee")
	public List<com.bean.domain.Project> activeProjectsForInvoiceByEmployee(@RequestParam(required = true) Long employeeId) {



		var activeProjects = projectRepository.findAllProjectsByEmployee(employeeId);
		logger.info(activeProjects.toString());
		List<com.bean.domain.Project> flattenProjects = new ArrayList<>();
		for (Project project : activeProjects)
			project.getBillRates().forEach(billrate -> {
				flattenProjects.addAll(projectService.createProjectForInvoice(project, billrate));
			});

		/*
		 * flattenProjects.forEach(project -> { System.out.println(project); });
		 */
		flattenProjects.forEach(project -> {

		});

		return flattenProjects;
	}
	@GetMapping("/allActiveProjects")
	public List<com.bean.domain.Project> getAllActiveProjects() {

		//logger.info("endDate: " + endDate);
		String endDate=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String selectedDate=LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		var activeProjects = projectRepository.findAllActiveProjectsByDate(endDate);
		logger.info(activeProjects.toString());
		List<com.bean.domain.Project> flattenProjects = new ArrayList<>();
		activeProjects.stream().forEach(project -> {
			project.getBillRates().forEach(billrate -> {
				flattenProjects.add(projectService.createProject(project, billrate, selectedDate));
			});
		});

		/*
		 * flattenProjects.forEach(project -> { System.out.println(project); });
		 */

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
		System.out.println(projectIds);
		var totalWageFromAssignments=0;
		//var totalWageFromAssignments = assignmentRepository.getTotalWageByProjectIds(projectIds);
		
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

 Map<String,Long> getWages(Project project,Wage billrate){
	var assignments=project.getAssignments();
	Map<String,Long> assignmentMap=new HashMap<>();
	assignments.forEach(assignment ->{
		if((assignment.getStartDate().isEqual(billrate.getStartDate()) || assignment.getStartDate().isAfter(billrate.getStartDate()))
		&& (assignment.getEndDate().isEqual(billrate.getEndDate())  || assignment.getEndDate().isEqual(billrate.getEndDate()))){
			assignmentMap.put(assignment.getAssignmentType(), (long)assignment.getWage());
		}
	} );
	return assignmentMap;
}
	@GetMapping("/getProjects")
	public List<com.bean.domain.Project> getProjects() {
		var repoProjects = projectRepository.findAll();
		List<com.bean.domain.Project> flattenProjects = new ArrayList<>();
		repoProjects.stream().forEach(project -> {
			System.out.println(project.getAssignments());
			project.getBillRates().forEach(billrate -> {

				flattenProjects.add(projectService.createProject(project, billrate, null));
			});
		});

		/*
		 * flattenProjects.forEach(project -> { System.out.println(project); });
		 */
		if(!flattenProjects.isEmpty()) {
			logger.info("flattenProjects.size():: "+flattenProjects.size());
		}
		return flattenProjects;
	}

	public com.bean.domain.Project updateBillRate(com.bean.domain.Project project, Assignment assignment) {

		return project;
	}

	@PostMapping("" +
			"/saveOnBoardProject")
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
	@GetMapping("/projects")
	public ResponseEntity<List<Project>> getProjectByEmpId(@RequestParam Long employeeId) {
		List<Project> project = projectRepository.findAllProjectsByEmployee(employeeId);
				//.orElseThrow(() -> new ResourceNotFoundException("Project not exist with id :" + employeeId));
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
