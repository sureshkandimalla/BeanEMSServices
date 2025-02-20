package com.bean.controller;

import com.bean.domain.EmployeeReconcileRecord;
import com.bean.domain.ReconcileExpense;
import com.bean.domain.ReconcileRecord;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.*;
import com.bean.repository.*;
import com.bean.service.EmployeeMapper;
import com.bean.service.EmployeeReconcileRecordMapper;
import com.bean.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@CrossOrigin(origins = { "http://beanems.s3-website-us-east-1.amazonaws.com","http://localhost:3000", "http://localhost:4000", "http://localhost:3001" })
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
	@Autowired
	AdjustmentRepository adjustmentRepository;

	@Autowired
	InsuranceRepository insuranceRepository;

	/*
	 * get all employees
	 */

	@GetMapping("/getReconcileRecords/{employeeId}")
	public List<ReconcileRecord> getReconcileRecordsForEmployee(@PathVariable Long employeeId) {
		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with employeeId :" + employeeId));

		List<ReconcileRecord> reconcileRecords = getReconcileRecords(employeeId);

		return reconcileRecords;
	}
	@GetMapping("/getReconcileRecords")
	public List<EmployeeReconcileRecord> getReconcileRecords() {
		List<Employee> employees = employeeRepository.findAll();
		List<EmployeeReconcileRecord> employeeReconcileRecords = new ArrayList<>();
		for (Employee employee : employees) {
			List<ReconcileRecord> reconcileRecords = getReconcileRecords(employee.getEmployeeId());
			Long totalIncome = (long) reconcileRecords.stream()
					.mapToDouble(ReconcileRecord::getIncome)
					.sum();
			Long totalExpense = (long) reconcileRecords.stream()
					.mapToDouble(ReconcileRecord::getExpense)
					.sum();
			EmployeeReconcileRecord domainEmp= EmployeeReconcileRecordMapper.mapToReconcileRecord(employee,totalIncome,totalExpense);
			employeeReconcileRecords.add(domainEmp);
		}
		//List<ReconcileRecord> reconcileRecords = getReconcileRecords(employeeId);

		return employeeReconcileRecords;
	}


	private List<ReconcileRecord> getReconcileRecords( Long employeeId) {
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
		List<com.bean.domain.Adjustment> adjustments= adjustmentRepository.findAdjustmentsByEmployeeId(employeeId).stream()
				.map(row -> {
					com.bean.domain.Adjustment adjustment = new com.bean.domain.Adjustment(row);
					if (adjustment.getToId() == employeeId) {
						adjustment.setAmount(-Math.abs(adjustment.getAmount()));
					}
					return adjustment;
				})
				.collect(Collectors.toList());

		/*List<Insurance> insurances = insuranceRepository.findByEmployeeId(employeeId);
		insurances.stream().forEach(insurance -> {
			com.bean.domain.Adjustment adjustment = new com.bean.domain.Adjustment();
			adjustment.setAmount(insurance.getAmount());
			adjustment.setAdjustmentType("Insurance");
			adjustment.setAdjustmentDate(insurance.getMonth());
			adjustment.setNotes(insurance.getInsuranceType());
			adjustments.add(adjustment);

		});*/
		/*var x=adjustments.stream()
				.collect(Collectors.groupingBy(com.bean.domain.Adjustment::getToId,
						Collectors.groupingBy(com.bean.domain.Adjustment::getAdjustmentType)));

		x.forEach((toId,adjustmentTypeMap) -> {
			ReconcileRecord reconcileRecord = new ReconcileRecord();
			reconcileRecord.setDescription("Adjustments for "+employee.getFirstName()+" "+employee.getLastName());
			adjustmentTypeMap.forEach((adjustmentType,adjustmentsList) -> {
				ReconcileExpense reconcileExpense = new ReconcileExpense();
				reconcileExpense.setExpenseType(adjustmentType);
				reconcileExpense.setTotal((float) adjustmentsList.stream().mapToDouble(com.bean.domain.Adjustment::getAmount).sum());
				reconcileRecord.setIncome(reconcileRecord.getIncome()+reconcileExpense.getTotal());
				reconcileExpense.setStartDate(adjustmentsList.get(0).getAdjustmentDate());
				reconcileExpense.setEndDate(adjustmentsList.get(adjustmentsList.size()-1).getAdjustmentDate());
				reconcileExpense.setDescription(adjustmentType);
				if(reconcileRecord.getExpenseRecords() ==null)
					reconcileRecord.setExpenseRecords(new ArrayList<>());
				reconcileRecord.getExpenseRecords().add(reconcileExpense);
			});
			if(reconcileRecord.getIncome()<0) {
				reconcileRecord.setExpense(Math.abs(reconcileRecord.getIncome()));
				reconcileRecord.setIncome(0);
			}
			reconcileRecords.add(reconcileRecord);
		});*/
		return reconcileRecords;
	}



}
