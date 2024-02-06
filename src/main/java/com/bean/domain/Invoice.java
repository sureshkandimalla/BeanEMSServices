package com.bean.domain;

import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Invoice {
	
	
	 	private long invoiceId;
	    private Long projectId;
	    private LocalDate  invoiceMonth;
	    private Long billRate;
	    private Long hours;
	    private Long total;
	    private Long invoicePaidAmount;
	    private LocalDate invoiceDate;
	    private LocalDate startDate;
	    private LocalDate endDate;
	    private LocalDate paymentDate;
	    private String status;
	    private Long assignmentId;
	    private String formatSelectedDate;
	    
		@Override
		public String toString() {
			return "Invoice [invoiceId=" + invoiceId + ", projectId=" + projectId + ", invoiceMonth=" + invoiceMonth
					+ ", billing=" + billRate + ", hours=" + hours + ", total=" + total + ", invoicePaidAmount="
					+ invoicePaidAmount + ", invoiceDate=" + invoiceDate + ", startDate=" + startDate + ", endDate="
					+ endDate + ", paymentDate=" + paymentDate + ", status=" + status + ", assignmentId=" + assignmentId
					+ "]";
		}
	    
	    

}
