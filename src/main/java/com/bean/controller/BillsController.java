package com.bean.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bean.domain.BasicEmployee;
import com.bean.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bean.model.Bills;
import com.bean.repository.BillsRepository;
import com.bean.service.BillsService;


@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com","http://localhost:3000", "http://localhost:4000"})
@RestController
@RequestMapping("/api/v1/bills")
public class BillsController {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BillsController.class);
	
	@Autowired
    private BillsRepository billsRepository;
	@Autowired
    private BillsService billsService;
    @Autowired
    private EmployeeService employeeService;
	
	
    @GetMapping("/getBillsForMonthAndYear")
	public ResponseEntity<List<Bills>> getBillsForMonthAndYear(@RequestParam(required = true) String selectedDate) {
    	
    	LocalDate localDate = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    	logger.info("selectedDate:::::"+selectedDate);
        // Format the LocalDate to a string in "yyyymm" format
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Optional<List<Bills>> billsList= billsService.findBillsByInvoiceMonth(formattedDate);
        logger.info("billsList:: "+billsList.toString());
    	return billsList.map(bills -> ResponseEntity.ok(bills))
                .orElse(null);
    }
    @GetMapping("/getBillsForProject")
    public ResponseEntity<List<com.bean.domain.Bills>> getBillsForProject(@RequestParam(required = true) long projectId) {

        Optional<List<Bills>> billsList= billsService.findBillsForProject(projectId);
        List<com.bean.domain.Bills> domainBillsList = billsList.orElseThrow().stream()
                .map(bills -> new com.bean.domain.Bills(bills))
                .collect(Collectors.toList());
        List<BasicEmployee> employees=employeeService.getEmployees();
        domainBillsList.stream().forEach(bills -> {
                    bills.setEmployeeName(employees.stream()
                            .filter(emp -> emp.getEmployeeId()==bills.getEmployeeId())
                            .findFirst()
                            .map(BasicEmployee::getName)
                            .orElse("Unknown"));
                    logger.info("bills:: "+ bills);
        }   );
        logger.info("billsList:: "+ domainBillsList);
        return billsList.map(bills -> ResponseEntity.ok(domainBillsList))
                .orElse(null);
    }
    @GetMapping("/getBillsForEmployee")
    public ResponseEntity<List<com.bean.domain.Bills>> getBillsForEmployee(@RequestParam(required = true) long employeeId) {

        Optional<List<Bills>> billsList= billsService.findBillsForEmployee(employeeId);
        List<com.bean.domain.Bills> domainBillsList = billsList.orElseThrow().stream()
                .map(bills -> new com.bean.domain.Bills(bills))
                .collect(Collectors.toList());
        List<BasicEmployee> employees=employeeService.getEmployees();
        domainBillsList.stream().forEach(bills -> {
            bills.setEmployeeName(employees.stream()
                    .filter(emp -> emp.getEmployeeId()==bills.getEmployeeId())
                    .findFirst()
                    .map(BasicEmployee::getName)
                    .orElse("Unknown"));
            logger.info("bills:: "+ bills);
        }   );
        logger.info("billsList:: "+ domainBillsList);
        return billsList.map(bills -> ResponseEntity.ok(domainBillsList))
                .orElse(null);
    }

}
