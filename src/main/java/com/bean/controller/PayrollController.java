package com.bean.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bean.model.Payroll;
import com.bean.repository.PayrollRepository;
import com.bean.service.BillsService;


@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com","http://localhost:3000", "http://localhost:4000"})
@RestController
@RequestMapping("/api/v1/payroll")
public class PayrollController {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PayrollController.class);
	
	@Autowired
    private PayrollRepository payrollRepository;
	@Autowired
    private BillsService billsService;
	
	
    @GetMapping("/getPayrolls")
	public List<Payroll> getPayrolls() {

    	return payrollRepository.findAll();

    }
	@GetMapping("/getPayrollsForEmp")
	public List<Payroll> getPayrolls(@RequestParam(required = true) long employeeId) {

		List<Payroll> filteredPayrolls = payrollRepository.findPayrollsWithPayPeriodDatesByEmployeeId(employeeId);

		return filteredPayrolls;

	}

}
