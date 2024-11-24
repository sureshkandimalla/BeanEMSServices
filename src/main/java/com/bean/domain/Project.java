package com.bean.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter@Setter
public class Project {
    private long projectId;
    private String projectName;
    private long employeeId;
    private String employeeName;
    private String vendorName;
    private long vendorId;
    private String clientName;
    private String client; //for project table
    private long clientId;
    private LocalDate startDate;
    private LocalDate endDate;
    private float billRate;
    private float employeePay;
    private float expenseInternal;
    private float expenseExternal;
    private float net;
    private String status;
    private String  invoiceTerm;
    private String  paymentTerm;
    private float hours;
    private long invoiceId;
    private float Billing;
    private float total;
    
    
	@Override
	public String toString() {
		return "Project [projectId=" + projectId + ", projectName=" + projectName + ", employeeId=" + employeeId
				+ ", employeeName=" + employeeName + ", vendorName=" + vendorName + ", vendorId=" + vendorId
				+ ", clientName=" + clientName + ", clientId=" + clientId + ", startDate=" + startDate + ", endDate="
				+ endDate + ", billRate=" + billRate + ", employeePay=" + employeePay + ", expenseInternal="
				+ expenseInternal + ", expenseExternal=" + expenseExternal + ", net=" + net + ", status=" + status
				+ ", invoiceTerm=" + invoiceTerm + ", paymentTerm=" + paymentTerm + ", hours=" + hours + ", invoiceID="
				+ invoiceId + ", Billing=" + Billing + ", total=" + total + "]";
	}
    
    
  

    
}
