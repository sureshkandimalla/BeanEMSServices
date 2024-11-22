package com.bean.controller;

import com.bean.model.Adjustment;
import com.bean.model.Payroll;
import com.bean.repository.AdjustmentRepository;
import com.bean.repository.PayrollRepository;
import com.bean.service.BillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4000"})
@RestController
@RequestMapping("/api/v1/adjustment")
public class AdjustmentController {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AdjustmentController.class);
	
	@Autowired
    private AdjustmentRepository adjustmentRepository;

	
    @GetMapping("/getAdjustments")
	public List<Adjustment> getAdjustments() {

    	return adjustmentRepository.findAll();

    }
	@GetMapping("/findAdjustmentsByEmployeeId")
	public List<com.bean.domain.Adjustment> findAdjustmentsByEmployeeId(@RequestParam(required = true) long id) {

		List<Object[]> filteredAdjustments = adjustmentRepository.findAdjustmentsByEmployeeId(id);
		List<com.bean.domain.Adjustment> adjustments = filteredAdjustments.stream()
				.map(a -> new com.bean.domain.Adjustment(a)) // Replace with appropriate constructor
				.collect(Collectors.toList());
		return adjustments;

	}

}
