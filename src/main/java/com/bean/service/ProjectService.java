package com.bean.service;

import java.time.LocalDate;
import java.util.List;

import com.bean.model.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.domain.Status;
import com.bean.model.Invoice;
import com.bean.model.Project;
import com.bean.model.Wage;

@Service
public class ProjectService {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ProjectService.class);
	
    @Autowired
    private InvoiceService invoiceService;
    
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
		    if(wage.getEndDate().isBefore(LocalDate.now()) ||  project.getEndDate().isBefore(LocalDate.now())) //to recheck condn
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
}
