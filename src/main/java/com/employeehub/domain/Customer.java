package com.employeehub.domain;

import java.time.LocalDate;

public record Customer(Long customerId, String customerName, String ein, String phone, String emailId,
		LocalDate startDate, LocalDate endDate, String customerCompanyName, String webSite,
		String streetAddress, String streetAddress2, String city, String state, String zipCode, String country,
		Double creditLimit, String parentCompany, String billingContact, String apContact, String standardCurrency,
		String msaStatus, String defaultBillingMethod, String paymentTerms, String notes) {

}
