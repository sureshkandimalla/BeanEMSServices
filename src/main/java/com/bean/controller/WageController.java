package com.bean.controller;

import com.bean.exception.ResourceNotFoundException;
import com.bean.model.*;
import com.bean.repository.BillsRepository;
import com.bean.repository.ProjectRepository;
import com.bean.repository.WageRepository;
import com.bean.service.BillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4000"})
@RestController
@RequestMapping("/api/v1/wages")
public class WageController {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WageController.class);
	
	@Autowired
    private WageRepository wageRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/getAllWages")
    public List<Wage> getAllEmployees() {
        return wageRepository.findAll();
    }
    @PostMapping("/wage")
    public ResponseEntity<Project>  createAssignment(@RequestBody com.bean.domain.Wage domainWage) {

        System.out.println(domainWage);
        Project project = projectRepository.findById(domainWage.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not exist with id :" + domainWage.getProjectId()));
        Wage modelWage=new Wage();
        modelWage.setWage(domainWage.getWage());
        modelWage.setWageType(domainWage.getWageType());
        modelWage.setStartDate(domainWage.getStartDate());
        modelWage.setEndDate(domainWage.getEndDate());
        project.getBillRates().add(modelWage);
        modelWage.setCreatedDate(LocalDate.now());

        //return wageRepository.save(Wage);
        return ResponseEntity.ok( projectRepository.save(project));
    }

}
