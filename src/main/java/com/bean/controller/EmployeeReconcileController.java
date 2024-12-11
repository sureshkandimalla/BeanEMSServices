package com.bean.controller;

import com.bean.domain.ReconcileExpense;
import com.bean.domain.ReconcileRecord;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Employee;
import com.bean.model.Invoice;
import com.bean.model.Payroll;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.InvoiceRepository;
import com.bean.repository.PayrollRepository;
import com.bean.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
	@Autowired
	private PayrollRepository payrollRepository;

	/*
	 * get all employees
	 */

	@GetMapping("/getReconcileRecords/{employeeId}")
	public List<ReconcileRecord> getAllEmployees(@PathVariable Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with employeeId :" + employeeId));

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
				//reconcileRecord.setIncome((float) invoices.stream().mapToDouble(Invoice::getTotal).sum());
				reconcileRecord.setInvoiceTotal((float) invoices.stream().mapToDouble(Invoice::getTotal).sum());
				reconcileRecord.setInvoicePaidAmount((float) invoices.stream().mapToDouble(Invoice::getInvoicePaidAmount).sum());
				//reconcileRecord.setExpense(0);
				reconcileRecord.setStartDate(wage.getStartDate());
				reconcileRecord.setEndDate(wage.getEndDate());
				//reconcileRecord.setIncome(reconcileRecord.getIncome()-reconcileRecord.getExpense());
			});

		project.getAssignments().stream().forEach(assignment -> {
			if(assignment.getEmployeeId() != employeeId)
				return;
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
			reconcileRecord.setIncome(reconcileRecord.getIncome()+reconcileExpense.getTotal());
			reconcileExpense.setStatus(assignment.getStatus());
			reconcileExpense.setStartDate(assignment.getStartDate());
			reconcileExpense.setEndDate(assignment.getEndDate());
			if(reconcileRecord.getExpenseRecords() ==null)
				reconcileRecord.setExpenseRecords(new ArrayList<>());
			reconcileRecord.getExpenseRecords().add(reconcileExpense);
			});
		reconcileRecords.add(reconcileRecord);

		});
		// payrolls

		List<Payroll> filteredPayrolls = payrollRepository.findPayrollsWithPayPeriodDatesByEmployeeId(employeeId);

		Map<Integer,List<Payroll>> payrollMap=	filteredPayrolls.stream()
				.filter(payroll -> payroll.getCheckDate() != null)
				.collect(Collectors.groupingBy(payroll -> payroll.getCheckDate().getYear()));
		payrollMap.forEach((year,payrolls) -> {
			ReconcileRecord reconcileRecord = new ReconcileRecord();
			reconcileRecord.setDescription("Payroll for "+employee.getFirstName()+" "+employee.getLastName() + " for year "+year);
		//	reconcileRecord.setWage((float) payrolls.stream().mapToDouble(Payroll::getWage).sum());
			reconcileRecord.setHours((float) payrolls.stream().mapToDouble(Payroll::getHours).sum());
			//reconcileRecord.setProjectBilling(reconcileRecord.getWage());
			reconcileRecord.setExpense((float) payrolls.stream().mapToDouble(Payroll::getTotalPaid).sum());
			//reconcileRecord.setInvoiceTotal((float) payrolls.stream().mapToDouble(Payroll::getTotal).sum());
			//reconcileRecord.setInvoicePaidAmount((float) payrolls.stream().mapToDouble(Payroll::getBillPaidAmount).sum());
			//reconcileRecord.setExpense(0);
			reconcileRecord.setStartDate(payrolls.get(0).getPayCycleStartDate());
			reconcileRecord.setEndDate(payrolls.get(payrolls.size()-1).getPayCycleEndDate());
			reconcileRecords.add(reconcileRecord);
		});
		/*filteredPayrolls.forEach(payroll -> {
			ReconcileRecord reconcileRecord = new ReconcileRecord();
			reconcileRecord.setDescription("Payroll for "+payroll.getEmployee().getFirstName()+" "+payroll.getEmployee().getLastName());
			reconcileRecord.setWage(payroll.getWage());
			reconcileRecord.setHours(payroll.getHours());
			reconcileRecord.setProjectBilling(payroll.getWage());
			reconcileRecord.setIncome(payroll.getTotal());
			reconcileRecord.setInvoiceTotal(payroll.getTotal());
			reconcileRecord.setInvoicePaidAmount(payroll.getBillPaidAmount());
			reconcileRecord.setExpense(0);
			reconcileRecord.setStartDate(payroll.getStartDate());
			reconcileRecord.setEndDate(payroll.getEndDate());
			reconcileRecords.add(reconcileRecord);
		}*/
		return reconcileRecords;
	}


}
