package com.bean.controller;

import com.bean.exception.ResourceNotFoundException;
import com.bean.model.PotentialEmployee;
import com.bean.model.Project;
import com.bean.model.Wage;
import com.bean.repository.PemployeeRepository;
import com.bean.repository.ProjectRepository;
import com.bean.repository.WageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com","http://localhost:3000", "http://localhost:4000"})
@RestController
@RequestMapping("/api/v1/visa")
public class VisaController {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VisaController.class);
	
	@Autowired
    private PemployeeRepository pemployeeRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/getAllPotentialEmployees")
    public List<PotentialEmployee> getAllPotentialEmployees() {
        return pemployeeRepository.findAll();
    }
    @PostMapping("/potentialEmployees")
    public ResponseEntity<PotentialEmployee>  createPotentialEmployees(@RequestBody PotentialEmployee potentialEmployee) {

        System.out.println(potentialEmployee);
        return ResponseEntity.ok( pemployeeRepository.save(potentialEmployee));
    }
    @PostMapping("/savePotentialEmployees")
    public ResponseEntity<List<PotentialEmployee>>  createPotentialEmployees(@RequestBody List<PotentialEmployee> potentialEmployees) {
        return ResponseEntity.ok( pemployeeRepository.saveAll(potentialEmployees));
    }

}
