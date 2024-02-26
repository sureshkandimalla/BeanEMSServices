package com.bean.domain;

import java.time.LocalDate;

public record Employee(Long employeeId, String designation, String dob, String ssn, String phone, String emailId,
		String employmentType, String firstName, String lastName, String gender, LocalDate startDate, LocalDate endDate,
		String referredBy, String taxTerm, String visa, String address_line_1,String address_line_2, String city, String state, String country,
		String zipCode ) {

}
