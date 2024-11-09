package com.bean.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bean.model.Assignment;
import com.bean.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bean.model.Customer;
import com.bean.domain.Status;
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
		    projectDomain.setEmployeePay(wage.getWage());
		    projectDomain.setStartDate(wage.getStartDate());
		    projectDomain.setEndDate(wage.getEndDate());
		  LocalDate today = LocalDate.now();
		  if ((wage.getEndDate() != null && wage.getEndDate().isBefore(today)) ||
				  (project.getEndDate() != null && project.getEndDate().isBefore(today)))
			  projectDomain.setStatus(Status.INACTIVE.toString());
		    else
		      projectDomain.setStatus(Status.ACTIVE.toString());
		      //projectDomain.gete
		  projectDomain.setExpenseExternal((float) project.getAssignments().stream().mapToDouble(Assignment::getWage).sum());
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
