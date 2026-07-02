package com.bean.controller;

import com.bean.model.PotentialEmployee;
import com.bean.model.Visa;
import com.bean.repository.PemployeeRepository;
import com.bean.service.VisaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com", "http://localhost:3000", "http://localhost:4000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/v1/visa")
public class VisaController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VisaController.class);

    @Autowired
    private VisaService visaService;

    @Autowired
    private PemployeeRepository pemployeeRepository;

    // ── Visa CRUD ────────────────────────────────────────────────────────────────

    @GetMapping("/getAllVisas")
    public List<Visa> getAllVisas() {
        return visaService.getAllVisas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visa> getVisaById(@PathVariable Long id) {
        return visaService.getVisaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public List<Visa> getVisasByEmployee(@PathVariable Long employeeId) {
        return visaService.getVisasByEmployeeId(employeeId);
    }

    @GetMapping("/employee/{employeeId}/latest")
    public ResponseEntity<Visa> getLatestVisaByEmployee(@PathVariable Long employeeId) {
        return visaService.getLatestVisaByEmployeeId(employeeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/createVisa")
    public ResponseEntity<Visa> createVisa(@RequestBody Visa visa) {
        logger.info("Creating visa: {}", visa);
        return ResponseEntity.ok(visaService.saveVisa(visa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Visa> updateVisa(@PathVariable Long id, @RequestBody Visa visaDetails) {
        logger.info("Updating visa id={}", id);
        return ResponseEntity.ok(visaService.updateVisa(id, visaDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteVisa(@PathVariable Long id) {
        visaService.deleteVisa(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    // ── Potential Employees ──────────────────────────────────────────────────────

    @GetMapping("/getAllPotentialEmployees")
    public List<PotentialEmployee> getAllPotentialEmployees() {
        return pemployeeRepository.findAll();
    }

    @PostMapping("/potentialEmployees")
    public ResponseEntity<PotentialEmployee> createPotentialEmployee(@RequestBody PotentialEmployee potentialEmployee) {
        logger.info("Creating potential employee: {}", potentialEmployee);
        return ResponseEntity.ok(pemployeeRepository.save(potentialEmployee));
    }

    @PostMapping("/savePotentialEmployees")
    public ResponseEntity<List<PotentialEmployee>> savePotentialEmployees(@RequestBody List<PotentialEmployee> potentialEmployees) {
        return ResponseEntity.ok(pemployeeRepository.saveAll(potentialEmployees));
    }
}
