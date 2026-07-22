package com.bean.domain;

import java.time.LocalDate;

public record Customer(Long customerId, String vendorName, String ein, String phone, String emailId,
		LocalDate startDate, LocalDate endDate, String vendorCompanyName, String webSite,
		String streetAddress, String streetAddress2, String city, String state, String zipCode, String country) {

}
