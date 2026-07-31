package com.employeehub.domain;

import java.time.LocalDate;

public record Customer(Long customerId, String customerName, String ein, String phone, String emailId,
		LocalDate startDate, LocalDate endDate, String customerCompanyName, String webSite,
		String streetAddress, String streetAddress2, String city, String state, String zipCode, String country) {

}
