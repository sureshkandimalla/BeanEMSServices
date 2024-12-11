package com.bean.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bean.exception.BillsException;
import com.bean.exception.InvoiceException;
import com.bean.model.Assignment;
import com.bean.model.Bills;
import com.bean.model.Invoice;
import com.bean.repository.AssignmentRepository;
import com.bean.repository.BillsRepository;
import com.bean.repository.InvoiceRepository;

@Service
public class InvoiceService {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InvoiceService.class);
	
	@Autowired
    private InvoiceRepository invoiceRepository;
	
	@Autowired
    private AssignmentRepository assignmentRepository;
	
	@Autowired
    private BillsRepository billsRepository;
	
	@Transactional
	public Invoice createInvoiceObject(com.bean.domain.Invoice dbInvoice, String Date)
			throws InvoiceException, BillsException {
		// TODO Auto-generated method stub

		LocalDate today = LocalDate.now();
		// Format the date as "yyyy-MM-dd"
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedDate = today.format(formatter);
		LocalDate parsedDate = LocalDate.parse(formattedDate, formatter);
		//LocalDate invoiceMonth = LocalDate.parse(, formatter);
		String invoiceMonthForDB = dbInvoice.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMM"));

		Invoice dbInvoiceObject =null;
		Invoice invoice = new Invoice();

		logger.info("dbInvoice.getInvoiceId(): " + dbInvoice.getInvoiceId() + " " + invoiceMonthForDB);
		Optional<Invoice> existingInvoice = invoiceRepository.findByInvoiceByMonthAndInvoiceId(invoiceMonthForDB,
				dbInvoice.getInvoiceId());

		logger.info("existingInvoice.isPresent():: " + existingInvoice.isPresent());
		if (existingInvoice.isPresent()) {

			Invoice existing = existingInvoice.get();
			if (!(existing.getHours()==dbInvoice.getHours()) || !(existing.getTotal()==dbInvoice.getTotal())) {
				existing.setHours(dbInvoice.getHours());
				existing.setTotal(dbInvoice.getTotal());
				// toadd params if required invoice_date,invoice paid amount, need to be
				// updated?

				try {
					dbInvoiceObject = invoiceRepository.save(existing); // update if existing invoiceid and month
				} catch (Exception e) {
					throw new InvoiceException("Failed to save invoice", e);
				}

			}

		} else {

			invoice.setHours(dbInvoice.getHours());
			invoice.setInvoiceId(dbInvoice.getInvoiceId());
			invoice.setTotal(dbInvoice.getTotal());
			invoice.setInvoiceDate(parsedDate);
			invoice.setStartDate(dbInvoice.getStartDate());
			invoice.setEndDate(dbInvoice.getEndDate());
			invoice.setStatus("Created"); // tochange as per business
			invoice.setPaymentDate(parsedDate);
			invoice.setProjectId(dbInvoice.getProjectId());
			invoice.setBilling(dbInvoice.getBillRate());
			invoice.setInvoicePaidAmount(dbInvoice.getInvoicePaidAmount());
			invoice.setInvoiceMonth(dbInvoice.getStartDate()); // tochange as per business

			try {
				dbInvoiceObject = invoiceRepository.save(invoice);
			} catch (Exception e) {

				// You may want to provide more specific exception handling based on the type of
				// exception thrown
				throw new InvoiceException("Failed to save invoice", e);
			}
		}

		logger.info("invoice dbobj:: " + dbInvoiceObject);
		if (dbInvoiceObject != null && isValid(dbInvoiceObject.getInvoiceId())) {
			try {
				//List<Assignment> assignmentsList = assignmentRepository.findByProjectId(dbInvoice.getProjectId());
				//logger.info("assignmentsList size" + assignmentsList.size());
				// Map Assignments data to Bills object and save to Bills table
				LocalDate invoiceMonth = dbInvoiceObject.getInvoiceMonth();
				List<Assignment> assignmentsList = assignmentRepository.findByProjectId(dbInvoice.getProjectId()).stream()
						.filter(assignment -> assignment.getStartDate() != null && assignment.getEndDate() != null)
						.filter(assignment -> !assignment.getStartDate().isAfter(invoiceMonth)
								&& !assignment.getEndDate().isBefore(invoiceMonth))
						.collect(Collectors.toList());

				for (Assignment assignment : assignmentsList) {
					Bills bill = mapAssignmentsToBills(assignment, dbInvoiceObject); // can use invoice obj as well
					billsRepository.save(bill);
				}
			} catch (Exception e) {
				throw new BillsException("Failed to save bills", e);
			}
		} else {

			// You may throw an exception, log an error, or handle it based on application's
			// logic
		}

		return dbInvoiceObject;
	}
	
	  private Bills mapAssignmentsToBills(Assignment assignment, Invoice invoice) {

	        // Create a Bills object and map data from Assignments
	        Bills bill = new Bills();
	        bill.setInvoiceId(invoice.getInvoiceId());
	        bill.setAssignmentId(assignment.getAssignmentId());
	        bill.setEmployeeId(assignment.getEmployeeId());
	        bill.setBillDate(invoice.getInvoiceDate());
	        bill.setBilling((long)assignment.getWage());
	        bill.setHours(invoice.getHours());
	        bill.setBillPaidAmount(0);
	        bill.setPaymentDate(invoice.getPaymentDate());
	        bill.setStartDate(invoice.getStartDate());
	        bill.setEndDate(invoice.getStartDate());
	        bill.setInvoiceMonth(invoice.getInvoiceMonth());
	        bill.setStatus(assignment.getStatus());
	        bill.setTotal(bill.getHours()*bill.getBilling());
		  bill.setBillType(assignment.getAssignmentType() + " - " + assignment.getDescription()+" - for Project "+assignment.getProjectId());

	        logger.info("billobj to be saved:: "+bill.toString());
	        return bill;
	    }

	  public List<Invoice> getInvoiceByMonthAndProjectId(String selectedDate, Long projectId) {
			 logger.info(selectedDate+ ":: "+projectId);
			 LocalDate date = LocalDate.parse(selectedDate);
		     String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMM"));

			 return invoiceRepository.findByInvoiceByMonthAndProjectId(formattedDate, projectId);
			 
		}
	public List<Invoice> getInvoiceByProjectId( Long projectId) {
		logger.info( "Project ID :: "+projectId);

		return invoiceRepository.findByProjectId( projectId);

	}
	  
	  public boolean isValid(Long value) {
	        return value != null && value != 0L && value != Long.MIN_VALUE;
	    }
	  
}
