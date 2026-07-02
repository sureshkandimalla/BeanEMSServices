package com.bean.controller;

import com.bean.model.LCA;
import com.bean.service.LCAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com", "http://localhost:3000", "http://localhost:4000", "http://localhost:3001"})
@RestController
@RequestMapping("/api/v1/lca")
public class LCAController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(LCAController.class);

    @Autowired
    private LCAService lcaService;

    @GetMapping
    public List<LCA> getAllLCAs() {
        return lcaService.getAllLCAs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LCA> getLCAById(@PathVariable Long id) {
        return lcaService.getLCAById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employee/{employeeId}")
    public List<LCA> getLCAsByEmployeeId(@PathVariable Long employeeId) {
        return lcaService.getLCAsByEmployeeId(employeeId);
    }

    @PostMapping("/save")
    public ResponseEntity<LCA> saveLCA(@RequestBody LCA lca) {
        boolean isUpdate = lca.getLcaId() != 0;
        logger.info("{} LCA id={}", isUpdate ? "Updating" : "Creating", lca.getLcaId());
        return ResponseEntity.ok(lcaService.saveLCA(lca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteLCA(@PathVariable Long id) {
        lcaService.deleteLCA(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
