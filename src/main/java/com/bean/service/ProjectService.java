package com.bean.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bean.domain.WageType;
import com.bean.model.Assignment;
import com.bean.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bean.model.Customer;
import com.bean.domain.ProjectStatus;
import com.bean.model.Invoice;
import com.bean.model.Project;
import com.bean.model.Wage;
import com.bean.repository.CustomerRepository;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.ProjectRepository;

@Service
public class ProjectService {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProjectService.class);
	
    @Autowired
    private InvoiceService invoiceService;
    
	@Autowired
    private EmployeeRepository employeeRepository;
	@Autowired
    private ProjectRepository projectRepository;
	@Autowired
    private CustomerRepository customerRepository;
    
	  public com.bean.domain.Project  createProject(Project project, Wage wage, String selectedDate){
		    com.bean.domain.Project projectDomain=new com.bean.domain.Project();
		    projectDomain.setProjectId(project.getProjectId());
		    projectDomain.setWageId(wage.getWageId());
		    projectDomain.setProjectName(project.getProjectName());
		    projectDomain.setClientName(project.getClient());
		    projectDomain.setInvoiceTerm(project.getInvoiceTerm());
		    projectDomain.setPaymentTerm(project.getPaymentTerm());
		    projectDomain.setStartDate(wage.getStartDate());
		    projectDomain.setEndDate(wage.getEndDate());
		    projectDomain.setBillRate(wage.getWage());
		    projectDomain.setEmployeeId(project.getEmployee().getEmployeeId());
		    projectDomain.setEmployeeName(project.getEmployee().getFirstName()+" "+project.getEmployee().getLastName());
		    projectDomain.setVendorId(project.getCustomer().getCustomerId());
		    projectDomain.setVendorName(project.getCustomer().getCustomerCompanyName());
		    projectDomain.setStartDate(wage.getStartDate());
		    projectDomain.setEndDate(wage.getEndDate());
		  LocalDate today = LocalDate.now();
		  // to get multiple assignments if employee pay if we need to create 2 diffrent projects if employee pay is diff
		  /*List<Assignment> empPayAssignments = project.getAssignments().stream()
				  .filter(assignment -> WageType.EMP_PAY.equals(assignment.getAssignmentType()))
				  .collect(Collectors.toList());*/
		  //TODO check for employee wages if there are multiple
		  Optional<Assignment> empAssignment = project.getAssignments().stream()
				  .filter(assignment -> (WageType.EMP_PAY.toString().equals(assignment.getAssignmentType()) && assignment.getStatus().equalsIgnoreCase("Active")))
				  .findFirst()
				  .or(() -> project.getAssignments().stream()
						  .filter(assignment -> WageType.EMP_PAY.toString().equals(assignment.getAssignmentType()))
						  .max(Comparator.comparing(Assignment::getStartDate))
				  );

		  //projectDomain.setEmployerTax((float) (projectDomain.getEmployeePay()*0.8));
		  /*Optional<Double> empPayWage = project.getAssignments().stream()
				  .filter(assignment -> (WageType.EMP_PAY.toString().equals(assignment.getAssignmentType())))
				  *//*.filter(assignment -> {
					  return (assignment.getStartDate().isAfter(project.getStartDate()) || assignment.getStartDate().isEqual(project.getStartDate())) &&
							  (assignment.getEndDate().isBefore(today) || assignment.getEndDate().isEqual(today));
				  })*//*
				  .map(Assignment::getWage)
				  .findFirst();*/
		  if(empAssignment.isPresent()) {
			  projectDomain.setEmployeePay(((float) empAssignment.get().getWage()));
			 if( empAssignment.get().getAssignmentTaxType().equals("W2")) {
				 Optional<Employee> employee = employeeRepository.findById(empAssignment.get().getEmployeeId());
				 if(employee.isPresent() && employee.get().getVisa().contains("OPT"))
					 projectDomain.setEmployerTax(0);
				 else
					 projectDomain.setEmployerTax((float) ((projectDomain.getEmployeePay() == 0) ? 0L : projectDomain.getEmployeePay() * .08));
				 projectDomain.setExpenseInternal(projectDomain.getEmployerTax());
			 }
		  }

		  //project.getAssignments().stream().forEach(assignment ->{if(WageType.EMP_PAY.equals(assignment.getAssignmentType())?assignment.getWage()});
		  // A manually-set status (via the Projects grid) always wins; only
		  // fall back to auto-computing Active/Inactive from the dates when
		  // the project has never had an explicit status set.
		  if (project.getStatus() != null && !project.getStatus().isBlank()) {
			  projectDomain.setStatus(project.getStatus());
		  } else if ((wage.getEndDate() != null && wage.getEndDate().isBefore(today)) ||
				  (project.getEndDate() != null && project.getEndDate().isBefore(today))) {
			  projectDomain.setStatus(ProjectStatus.INACTIVE.toString());
		  } else {
			  projectDomain.setStatus(ProjectStatus.ACTIVE.toString());
		  }
		  projectDomain.setExpenseExternal((float) project.getAssignments().stream()
				  .filter(assignment -> !WageType.EMP_PAY.toString().equals(assignment.getAssignmentType()))  // Exclude EMP_PAY
				  .mapToDouble(Assignment::getWage)  // Sum wages
				  .sum());
		  projectDomain.setNet(projectDomain.getBillRate()- projectDomain.getEmployeePay()- projectDomain.getExpenseExternal()- projectDomain.getExpenseInternal());
		//  projectDomain.setExpenseExternal((float) project.getAssignments().stream().mapToDouble(Assignment::getWage).sum());
		//  getExpense(wage,project.getAssignments());
		    
		    //to add cond'n if any requred for below other than just selectedDate
			if (selectedDate != null) {
				List<Invoice> monthlyInvoices = invoiceService.getInvoiceByMonthAndProjectId(selectedDate,
						project.getProjectId());

				
				monthlyInvoices.stream().filter(invoice -> invoice.getProjectId().equals(project.getProjectId()))
						.findAny().ifPresentOrElse(invoice -> {
							logger.info("id: "+invoice.getInvoiceId()+"hours: "+invoice.getHours());
							projectDomain.setHours(invoice.getHours());
							projectDomain.setInvoiceId(invoice.getInvoiceId());
							projectDomain.setTotal(invoice.getTotal());

						}, () -> {System.out.println("No matching invoice found");});
				 
				
				logger.info("monthlyInvoices::: "+monthlyInvoices.size());


			}
		    return projectDomain;

		  }


	public List<com.bean.domain.Project>  createProjectForInvoice(Project project, Wage wage){
		com.bean.domain.Project projectDomain=new com.bean.domain.Project();
		projectDomain.setProjectId(project.getProjectId());
		projectDomain.setProjectName(project.getProjectName());
		projectDomain.setClientName(project.getClient());
		projectDomain.setInvoiceTerm(project.getInvoiceTerm());
		projectDomain.setPaymentTerm(project.getPaymentTerm());
		projectDomain.setWeekStartDay(resolveWeekStartDay(project).name());
		projectDomain.setStartDate(wage.getStartDate());
		projectDomain.setEndDate(wage.getEndDate());
		projectDomain.setBillRate(wage.getWage());
		projectDomain.setEmployeeId(project.getEmployee().getEmployeeId());
		projectDomain.setEmployeeName(project.getEmployee().getFirstName()+" "+project.getEmployee().getLastName());
		projectDomain.setVendorId(project.getCustomer().getCustomerId());
		projectDomain.setVendorName(project.getCustomer().getCustomerCompanyName());
		projectDomain.setStartDate(wage.getStartDate());
		projectDomain.setEndDate(wage.getEndDate());
		LocalDate today = LocalDate.now();


		//Here
		LocalDate startDate = wage.getStartDate();
		LocalDate endDate = wage.getEndDate();
		if (endDate == null || endDate.isAfter(today)) {
			endDate = today;
		}
		System.out.println("startDate: "+startDate+" endDate: "+endDate);
		List<com.bean.domain.Project> dominList = new ArrayList<>();

		List<Invoice> projectInvoices = invoiceService.getInvoiceByProjectId(
				project.getProjectId());

		String invoiceTerm = project.getInvoiceTerm();
		DayOfWeek weekStartDay = resolveWeekStartDay(project);

		if ("1".equals(invoiceTerm)) {
			// Weekly: one row per calendar week, anchored to the project's
			// configured week-start day (defaults to Monday).
			addWeekAlignedRows(dominList, projectDomain, project, wage, today, projectInvoices,
					startDate, endDate, weekStartDay, 1, 6);
		} else if ("2".equals(invoiceTerm)) {
			// Biweekly: one row per two-week span, same anchor day.
			addWeekAlignedRows(dominList, projectDomain, project, wage, today, projectInvoices,
					startDate, endDate, weekStartDay, 2, 13);
		} else if ("6".equals(invoiceTerm)) {
			// Once in 4 Weeks: one row per four-week span, same anchor day.
			addWeekAlignedRows(dominList, projectDomain, project, wage, today, projectInvoices,
					startDate, endDate, weekStartDay, 4, 27);
		} else if ("5".equals(invoiceTerm)) {
			// Semi-Monthly: two rows per month — 1st-15th and 16th-end.
			LocalDate monthCursor = startDate.withDayOfMonth(1);
			while (!monthCursor.isAfter(endDate)) {
				LocalDate firstHalfStart = monthCursor;
				LocalDate firstHalfEnd = monthCursor.withDayOfMonth(15);
				dominList.add(buildInvoiceRow(projectDomain, project, wage, today, firstHalfStart, firstHalfEnd,
						projectInvoices.stream().filter(invoice -> firstHalfStart.equals(invoice.getStartDate())).findAny()));

				LocalDate secondHalfStart = monthCursor.withDayOfMonth(16);
				LocalDate secondHalfEnd = monthCursor.withDayOfMonth(monthCursor.lengthOfMonth());
				dominList.add(buildInvoiceRow(projectDomain, project, wage, today, secondHalfStart, secondHalfEnd,
						projectInvoices.stream().filter(invoice -> secondHalfStart.equals(invoice.getStartDate())).findAny()));

				monthCursor = monthCursor.plusMonths(1);
			}
		} else {
			// Monthly (also the fallback for "Special"/unrecognized terms).
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
			LocalDate current = startDate;
			while (!current.isAfter(endDate)) {
				LocalDate periodStart = current.withDayOfMonth(1); // first day of the current month
				LocalDate periodEnd = current.withDayOfMonth(current.lengthOfMonth()); // last day of the current month
				String selectedMonthYear = current.format(formatter);

				Optional<Invoice> matched = projectInvoices.stream()
						.filter(invoice -> invoice.getInvoiceMonth().format(formatter).equals(selectedMonthYear))
						.findAny();

				dominList.add(buildInvoiceRow(projectDomain, project, wage, today, periodStart, periodEnd, matched));
				current = current.plusMonths(1);
			}
		}

		return dominList;

	}

	// Shared by Weekly/Biweekly/Once-in-4-Weeks: all three are spans of a
	// fixed number of weeks anchored to the project's configured
	// week-start day, matched against any existing invoice by its exact
	// period start date.
	private void addWeekAlignedRows(List<com.bean.domain.Project> dominList, com.bean.domain.Project projectDomain,
			Project project, Wage wage, LocalDate today, List<Invoice> projectInvoices,
			LocalDate startDate, LocalDate endDate, DayOfWeek weekStartDay, int stepWeeks, int spanDays) {
		LocalDate cursor = startDate.with(TemporalAdjusters.previousOrSame(weekStartDay));
		while (!cursor.isAfter(endDate)) {
			final LocalDate periodStart = cursor;
			LocalDate periodEnd = periodStart.plusDays(spanDays);

			Optional<Invoice> matched = projectInvoices.stream()
					.filter(invoice -> periodStart.equals(invoice.getStartDate()))
					.findAny();

			dominList.add(buildInvoiceRow(projectDomain, project, wage, today, periodStart, periodEnd, matched));
			cursor = cursor.plusWeeks(stepWeeks);
		}
	}

	// Every week-based term (Weekly/Biweekly/Once-in-4-Weeks) anchors to
	// this project's own configured start-of-week day; defaults to Monday
	// when not set (covers both a genuinely unconfigured project and any
	// unparseable/legacy value, rather than throwing).
	private DayOfWeek resolveWeekStartDay(Project project) {
		String configured = project.getWeekStartDay();
		if (configured == null || configured.isBlank()) {
			return DayOfWeek.MONDAY;
		}
		try {
			return DayOfWeek.valueOf(configured.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			return DayOfWeek.MONDAY;
		}
	}

	// One row for a single invoice period (a month, or a week-based term's
	// span) — shared by both the monthly and week-aligned branches above.
	private com.bean.domain.Project buildInvoiceRow(com.bean.domain.Project template, Project project, Wage wage,
			LocalDate today, LocalDate periodStart, LocalDate periodEnd, Optional<Invoice> matchedInvoice) {
		com.bean.domain.Project row = template.clone();
		row.setStartDate(periodStart);
		row.setEndDate(periodEnd);

		if ((wage.getEndDate() != null && wage.getEndDate().isBefore(today)) ||
				(project.getEndDate() != null && project.getEndDate().isBefore(today)))
			row.setStatus(ProjectStatus.INACTIVE.toString());
		else
			row.setStatus(ProjectStatus.ACTIVE.toString());

		matchedInvoice.ifPresentOrElse(invoice -> {
			logger.info("id: " + invoice.getInvoiceId() + "hours: " + invoice.getHours());
			row.setHours(invoice.getHours());
			row.setInvoiceId(invoice.getInvoiceId());
			row.setTotal(invoice.getTotal());
		}, () -> logger.info("No matching invoice found for period {} - {}", periodStart, periodEnd));

		return row;
	}

	public ResponseEntity<String> saveProject(com.bean.domain.Project project) {

			logger.info("employeeid : "+project.getEmployeeId());
		
			Project dbProject = new Project();
			dbProject.setClient(project.getClient());
			dbProject.setEmployeeId(project.getEmployeeId());
			dbProject.setVendorId(project.getVendorId());
			dbProject.setEndDate(project.getEndDate());
			dbProject.setStartDate(project.getStartDate());
			dbProject.setStatus(project.getStatus());
			dbProject.setLastUpdated(LocalDate.now());
			dbProject.setProjectName(project.getProjectName());
			dbProject.setInvoiceTerm(project.getInvoiceTerm());
			dbProject.setPaymentTerm(project.getPaymentTerm());
			dbProject.setWeekStartDay(project.getWeekStartDay());

		Optional<Employee> optionalEmployee = employeeRepository.findById(project.getEmployeeId());
		Optional<Customer> optionalCustomer = customerRepository.findById(project.getVendorId());

		Wage wage =new Wage();
		wage.setEndDate(project.getEndDate());
		wage.setLastUpdated(LocalDate.now());
		wage.setStartDate(project.getStartDate());
		wage.setWageType("Billing");
		wage.setWage(project.getBillRate());
		wage.setCreatedDate(LocalDate.now());
		// Using Optional to handle null list gracefully
		Optional.ofNullable(dbProject.getBillRates())
				.orElseGet(() -> {
					List<Wage> newList = new ArrayList<>();
					dbProject.setBillRates(newList);
					return newList;
				})
				.add(wage);
		if (optionalEmployee.isPresent() && optionalCustomer.isPresent()) {
		    Employee employee = optionalEmployee.get();
		    Customer customer = optionalCustomer.get();

		    dbProject.setEmployee(employee);
		    dbProject.setCustomer(customer);

		    logger.info("employeeid & vendorid in project table: " + project.getEmployeeId() + " " + project.getVendorId());
		    projectRepository.save(dbProject);
		} else {
		    // Handle the case where either the employee or the customer is not found
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee or Customer not found");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Project data saved Succesfully");
		
	}
}
