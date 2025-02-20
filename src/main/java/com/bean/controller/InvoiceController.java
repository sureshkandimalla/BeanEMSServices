package com.bean.controller;

import com.bean.domain.DashboardEmployeeDetails;
import com.bean.model.Assignment;
import com.bean.repository.AssignmentRepository;
import com.bean.repository.InvoiceRepository;
import com.bean.service.InvoiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bean.exception.BillsException;
import com.bean.exception.InvoiceException;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Invoice;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

//import static org.graalvm.compiler.nodes.calc.BinaryArithmeticNode.ReassociateMatch.x;

@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com","http://localhost:3000", "http://localhost:3001","http://localhost:4000" })
@RestController
@RequestMapping("/api/v1/invoice")
public class InvoiceController {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InvoiceController.class);

	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private AssignmentRepository assignmentRepository;
	@Autowired
	private InvoiceService invoiceService;

	@PostMapping("/addInvoices")
	public ResponseEntity<String> createInvoice(@RequestBody List<com.bean.domain.Invoice> invoices)
			throws InvoiceException, BillsException {

		List<com.bean.domain.Invoice> filteredInvoices = invoices.stream()
				.filter(invoice -> isValid(invoice.getInvoiceId())) // only invoiceid as hours and total can be 0/null
				.collect(Collectors.toList());

		// filteredInvoices.forEach(System.out::println);
		logger.info("size:: " + filteredInvoices.size());
		for (com.bean.domain.Invoice invoice : filteredInvoices) {
			String formatSelectedDate = invoice.getFormatSelectedDate();
			logger.info("formatSelectedDate: " + formatSelectedDate);
			invoiceService.createInvoiceObject(invoice, formatSelectedDate);
			// invoiceRepository.save(dbInvoiceObject);
		}

		return new ResponseEntity<>("Invoices created successfully", HttpStatus.CREATED);
	}

	public boolean isValid(Long value) {
		return value != null && value != 0L && value != Long.MIN_VALUE;
	}
	

	@GetMapping("/invoices/{id}")
	public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not exist with id: " + id));
		return ResponseEntity.ok(invoice);
	}
	

	@GetMapping("/invoicesForMonth")
	public ResponseEntity<List<Invoice>> getInvoiceForMonth(String month) {
		/*
		 * Invoice invoice = invoiceRepository.findById(id) .orElseThrow(() -> new
		 * ResourceNotFoundException("Invoice not exist with id: " + id));
		 */
		month = "202303";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
		YearMonth yearMonth = YearMonth.parse(month, formatter);

		LocalDate startMonth = yearMonth.atDay(1);
		LocalDate endMonth = yearMonth.atEndOfMonth();
		List<Assignment> assignmentList = assignmentRepository.findAllActiveAssignment(month, month);
		List x = assignmentList.stream().map(assignment -> {
			Invoice newInvoice = new Invoice(startMonth, endMonth);
			// newInvoice.setAssignment(assignment);
			invoiceRepository.save(newInvoice);
			return newInvoice;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(x);
	}


	@GetMapping("/getAllInvoices")
	public ResponseEntity<List<Invoice>> getAllInvoices() {
		return ResponseEntity.ok(invoiceRepository.getAllInvoices());
	}

	
	@GetMapping("/invoicesForMonthAndYear")
	public ResponseEntity<List<Invoice>> getInvoiceForMonthAndYear(@RequestParam(required = true) String selectedDate, @RequestParam(required = false) String status) {

		logger.info("selectedDate:::::" + selectedDate);
		logger.info("status:::::" + status);
		LocalDate localDate = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
		logger.info("formattedDate:::::" + formattedDate);
		List<Invoice> invoiceMonthList;
		
		if(status.equalsIgnoreCase("viewAll")) {
		 invoiceMonthList = invoiceRepository.findAllInvoicesForTheMonth(formattedDate);
		}else {
			invoiceMonthList = invoiceRepository.findAllInvoicesForTheMonthAndStatus(formattedDate,status);
		}
		
		//to add logic based on status
		
		logger.info("invoiceMonthList:: " + invoiceMonthList.toString());
		return ResponseEntity.ok(invoiceMonthList);
	}
	
	
	/*
	 * get all employees count by status returns key-value of all emp with status
	 */

	@GetMapping("/invoicesCountByStatus")
	public List<Map<String, String>> getInvoicesCountByStatus() {

		List<Map<String, String>> invoiceStatus = invoiceRepository.getInvoiceCountByStatus();
		logger.info("invoiceStatus:: " + invoiceStatus.toString());
		return invoiceStatus;
	}

	

	@PutMapping("/invoices/{id}")
	public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoiceDetails) {
		Invoice invoice = invoiceRepository.findByInvoiceId(id).orElseThrow(() -> new ResourceNotFoundException("Invoice not exist with id: " + id));
		switch (invoiceDetails.getStatus().toLowerCase()) {
			case "paid":
				if (invoiceDetails.getInvoicePaidAmount() == 0) {
					invoiceDetails.setInvoicePaidAmount(invoiceDetails.getTotal());
				}
				break;
			case "unpaid":
				if (invoiceDetails.getInvoicePaidAmount() != 0) {
					invoiceDetails.setInvoicePaidAmount(0L);
				}
				break;
			default:
				// Handle other statuses if needed
				break;
		}
		Invoice updatedInvoice = invoiceRepository.saveAndFlush(invoiceDetails);
		return ResponseEntity.ok(updatedInvoice);
	}

	
	@DeleteMapping("/invoices/{id}")
	public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not exist with id: " + id));

		invoiceRepository.delete(invoice);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/getInvoices")
	public ResponseEntity<Invoice> getInvoices(@PathVariable Long id) {
		Invoice invoice = invoiceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not exist with id: " + id));
		return ResponseEntity.ok(invoice);
	}


	@GetMapping("/activeInvoices/{monthYear}")
	public ResponseEntity<List<Invoice>> getActiveInvoicesForMonth(@PathVariable String monthYear) {
		List<Invoice> invoiceList = invoiceRepository.findAllActiveInvoicesForTheMonth(monthYear, monthYear);
		return ResponseEntity.ok(invoiceList);
	}
	@GetMapping("/getInvoicesForEmployee")
	public ResponseEntity<List<Invoice>> getInvoicesForEmployee(@RequestParam(required = true) Long employeeId) {
		logger.info("employeeId:: "+employeeId);

		List<Invoice> invoiceList = invoiceRepository.findByEmployee(employeeId);
		return ResponseEntity.ok(invoiceList);
	}
	@GetMapping("/getInvoicesForProject")
	public ResponseEntity<List<Invoice>> getInvoicesForProject(@RequestParam(required = true) Long projectId) {
		logger.info("employeeId:: "+projectId);

		List<Invoice> invoiceList = invoiceRepository.findByProjectId(projectId).stream().toList();
		return ResponseEntity.ok(invoiceList);
	}



}
