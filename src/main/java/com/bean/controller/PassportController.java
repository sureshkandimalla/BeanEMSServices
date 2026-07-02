package com.bean.controller;

import com.bean.model.Passport;
import com.bean.service.PassportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com", "http://localhost:3000", "http://localhost:4000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/v1/passport")
public class PassportController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PassportController.class);

    @Autowired
    private PassportService passportService;

    @GetMapping("/getAllPassports")
    public List<Passport> getAllPassports() {
        return passportService.getAllPassports();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passport> getPassportById(@PathVariable Long id) {
        return passportService.getPassportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public List<Passport> getPassportsByEmployee(@PathVariable Long employeeId) {
        return passportService.getPassportsByEmployeeId(employeeId);
    }

    @PostMapping("/createPassport")
    public ResponseEntity<Passport> createPassport(@RequestBody Passport passport) {
        logger.info("Creating passport: {}", passport);
        return ResponseEntity.ok(passportService.savePassport(passport));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Passport> updatePassport(@PathVariable Long id, @RequestBody Passport passportDetails) {
        logger.info("Updating passport id={}", id);
        return ResponseEntity.ok(passportService.updatePassport(id, passportDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deletePassport(@PathVariable Long id) {
        passportService.deletePassport(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
