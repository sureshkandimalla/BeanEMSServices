package com.employeehub.controller;

import com.employeehub.exception.ResourceNotFoundException;
import com.employeehub.model.*;
import com.employeehub.repository.ProjectRepository;
import com.employeehub.repository.WageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


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
    public ResponseEntity<Project>  createAssignment(@RequestBody com.employeehub.domain.Wage domainWage) {

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

    @PutMapping("/{id}")
    public ResponseEntity<Wage> updateWage(@PathVariable long id, @RequestBody Wage wageDetails) {
        Wage wage = wageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Wage not exist with id :" + id));

        wage.setWage(wageDetails.getWage());
        wage.setStartDate(wageDetails.getStartDate());
        wage.setEndDate(wageDetails.getEndDate());

        return ResponseEntity.ok(wageRepository.save(wage));
    }

}
