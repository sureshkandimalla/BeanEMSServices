package com.bean.controller;

import com.bean.model.Adjustment;
import com.bean.repository.AdjustmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com","http://localhost:3000", "http://localhost:4000"})
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

		return adjustmentRepository.findAdjustmentsByEmployeeId(id).stream()
				.map(row -> {
					com.bean.domain.Adjustment adjustment = new com.bean.domain.Adjustment(row);
					if (adjustment.getToId() == id) {
						adjustment.setAmount(-Math.abs(adjustment.getAmount()));
					}
					return adjustment;
				})
				.collect(Collectors.toList());

	}
	@PostMapping("/addAdjustment")
	public ResponseEntity<Adjustment>  createInvoice(@RequestBody Adjustment adjustment) throws Exception {

		try {
			 Adjustment newAdjustment=adjustmentRepository.save(adjustment);

			return  ResponseEntity.ok(newAdjustment);
		} catch (Exception e) {
			throw new Exception("Adjustment data not saved", e);
		}
	}
}
