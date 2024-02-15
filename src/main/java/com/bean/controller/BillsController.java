package com.bean.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

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


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/bills")
public class BillsController {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BillsController.class);
	
	@Autowired
    private BillsRepository billsRepository;
	@Autowired
    private BillsService billsService;
	
	
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

}
