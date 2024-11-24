package com.bean.controller;

import com.bean.domain.ReconcileExpense;
import com.bean.domain.ReconcileRecord;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Employee;
import com.bean.model.Invoice;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.InvoiceRepository;
import com.bean.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4000", "http://localhost:3001" })
@RestController
@RequestMapping("/api/v1/reconcile")
public class EmployeeReconcileController {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmployeeReconcileController.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private InvoiceRepository invoiceRepository;

	/*
	 * get all employees
	 */

	@GetMapping("/getReconcileRecords/{id}")
	public List<ReconcileRecord> getAllEmployees(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

		List<ReconcileRecord> reconcileRecords = new ArrayList<>();
		employee.getProject().stream().forEach(project -> {

		ReconcileRecord reconcileRecord = new ReconcileRecord();
		reconcileRecord.setDescription("Consulting services provided for "+project.getCustomer().getCustomerCompanyName());
			project.getBillRates().stream().forEach(wage -> {
				reconcileRecord.setWage(wage.getWage());
				String startDate = wage.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMM"));
				String endDate = wage.getEndDate().format(DateTimeFormatter.ofPattern("yyyyMM"));
				var invoices=invoiceRepository.findAllInvoicesForTheMonthProject(startDate, endDate, project.getProjectId());
				reconcileRecord.setHours((float) invoices.stream().mapToDouble(Invoice::getHours).sum());
				reconcileRecord.setProjectBilling(wage.getWage());
				reconcileRecord.setIncome((float) invoices.stream().mapToDouble(Invoice::getTotal).sum());
				reconcileRecord.setInvoiceTotal((float) invoices.stream().mapToDouble(Invoice::getTotal).sum());
				reconcileRecord.setInvoicePaidAmount((float) invoices.stream().mapToDouble(Invoice::getInvoicePaidAmount).sum());
				reconcileRecord.setExpense(0);
				reconcileRecord.setStartDate(wage.getStartDate());
				reconcileRecord.setEndDate(wage.getEndDate());
				//reconcileRecord.setIncome(reconcileRecord.getIncome()-reconcileRecord.getExpense());
			});

		project.getAssignments().stream().forEach(assignment -> {
			ReconcileExpense reconcileExpense = new ReconcileExpense();
			reconcileExpense.setExpenseType(assignment.getAssignmentType());
			String startDate = assignment.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMM"));
			String endDate = assignment.getEndDate().format(DateTimeFormatter.ofPattern("yyyyMM"));
			var invoices=invoiceRepository.findAllInvoicesForTheMonthProject(startDate, endDate, assignment.getProjectId());
			reconcileExpense.setHours((float) invoices.stream().mapToDouble(Invoice::getHours).sum());
			reconcileExpense.setWage(assignment.getWage());
			reconcileExpense.setEmployeeId(assignment.getEmployeeId());
			reconcileExpense.setDescription(assignment.getDescription().concat(" for "+project.getProjectName()).concat(" @ $ "+assignment.getWage()));
			reconcileExpense.setTotal(reconcileExpense.getHours()*reconcileExpense.getWage());
			reconcileRecord.setExpense(reconcileRecord.getExpense()+reconcileExpense.getTotal());
			reconcileExpense.setStatus(assignment.getStatus());
			reconcileExpense.setStartDate(assignment.getStartDate());
			reconcileExpense.setEndDate(assignment.getEndDate());
			if(reconcileRecord.getExpenseRecords() ==null)
				reconcileRecord.setExpenseRecords(new ArrayList<>());
			reconcileRecord.getExpenseRecords().add(reconcileExpense);
			});
		reconcileRecords.add(reconcileRecord);

		});

		return reconcileRecords;
	}


}
