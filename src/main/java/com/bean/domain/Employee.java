package com.bean.domain;

import java.time.LocalDate;

public record Employee(Long employeeId, String designation, String dob, String ssn, String phoneNumber, String emailId,
					   String employmentType,String employeeType, String firstName, String lastName, String gender, LocalDate startDate, LocalDate endDate,
					   String referredBy, String taxTerms, String visa, String streetAddress, String address_line_2, String city, String state, String country,
					   String zipCode, String status, String location, Long income, Long expense, String everifyStatus, String i9, String PAF, String insurance,float annualPay,String companyName) {

}
