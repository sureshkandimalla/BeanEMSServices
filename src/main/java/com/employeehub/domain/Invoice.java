package com.employeehub.domain;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Invoice {
	
	
	 	// Server-assigned surrogate identity — null/absent on create, present
	 	// on rows read back from the DB. Never required from the client.
	 	private Long id;
	    // Cosmetic/business label the user types — not unique, not identity.
	    private Long invoiceNumber;
	    private Long projectId;
	    private LocalDate  invoiceMonth;
	    private float billRate;
	    private float hours;
	    private float total;
	    private float invoicePaidAmount;
	    private LocalDate invoiceDate;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private LocalDate paymentDate;
	    private String status;
	    private Long assignmentId;
	    private String formatSelectedDate;
	    
		@Override
		public String toString() {
			return "Invoice [id=" + id + ", invoiceNumber=" + invoiceNumber + ", projectId=" + projectId + ", invoiceMonth=" + invoiceMonth
					+ ", billing=" + billRate + ", hours=" + hours + ", total=" + total + ", invoicePaidAmount="
					+ invoicePaidAmount + ", invoiceDate=" + invoiceDate + ", startDate=" + startDate + ", endDate="
					+ endDate + ", paymentDate=" + paymentDate + ", status=" + status + ", assignmentId=" + assignmentId
					+ "]";
		}
	    
	    

}
