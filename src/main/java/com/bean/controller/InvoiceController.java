package com.bean.controller;

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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//import static org.graalvm.compiler.nodes.calc.BinaryArithmeticNode.ReassociateMatch.x;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class InvoiceController {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private InvoiceService invoiceService;
    
    @PostMapping("/addInvoices")
    public ResponseEntity<String> createInvoice(@RequestBody List<com.bean.domain.Invoice> invoices) throws InvoiceException, BillsException {
    	
		List<com.bean.domain.Invoice> filteredInvoices = invoices.stream().filter(
				invoice -> invoice.getHours() != null && Objects.nonNull(invoice.getInvoiceId()) && invoice.getTotal() != null)
				.collect(Collectors.toList());
		
		//filteredInvoices.forEach(System.out::println);
		logger.info("size:: "+filteredInvoices.size());
		for (com.bean.domain.Invoice invoice : filteredInvoices) {
			String formatSelectedDate =invoice.getFormatSelectedDate();
			logger.info("formatSelectedDate: "+formatSelectedDate);
			invoiceService.createInvoiceObject(invoice,formatSelectedDate);
			//invoiceRepository.save(dbInvoiceObject);
		}
		
    	return new ResponseEntity<>("Invoices created successfully", HttpStatus.CREATED);
    }



	@GetMapping("/invoices/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not exist with id: " + id));
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/invoicesForMonth")
    public ResponseEntity<List<Invoice>> getInvoiceForMonth(String month) {
        /* Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not exist with id: " + id));*/
        month = "202303";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        YearMonth yearMonth=YearMonth.parse(month,formatter);

        LocalDate startMonth = yearMonth.atDay(1);
        LocalDate endMonth = yearMonth.atEndOfMonth();
        List<Assignment> assignmentList=assignmentRepository.findAllActiveAssignment(month,month);
        List x=assignmentList.stream().map(assignment -> {
            Invoice newInvoice= new Invoice(startMonth,endMonth);
           // newInvoice.setAssignment(assignment);
            invoiceRepository.save(newInvoice);
            return newInvoice;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(x);
    }
    
    
    @GetMapping("/invoicesForMonthAndYear")
	public ResponseEntity<List<Invoice>> getInvoiceForMonthAndYear(@RequestParam(required = true) String selectedDate) {
    	
    	LocalDate localDate = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
logger.info("selectedDate:::::"+selectedDate);
        // Format the LocalDate to a string in "yyyymm" format
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
		/*
		 * logger.info("month:: "+month +"year:: "+year); String YearMonthReq =
		 * String.join("", year, month); if(YearMonthReq.length() <6) { YearMonthReq =
		 * String.join("0", year, month); //can be handled from UI }
		 */
    	
        List<Invoice> invoiceMonthList= invoiceRepository.findAllInvoicesForTheMonth(formattedDate);
        logger.info("invoiceMonthList:: "+invoiceMonthList.toString());
    	return ResponseEntity.ok(invoiceMonthList);
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @RequestBody Invoice invoiceDetails) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not exist with id: " + id));

   

        Invoice updatedInvoice = invoiceRepository.save(invoice);
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
        List<Invoice> invoiceList=invoiceRepository.findAllActiveInvoicesForTheMonth(monthYear,monthYear);

        return ResponseEntity.ok(invoiceList);
    }

}
