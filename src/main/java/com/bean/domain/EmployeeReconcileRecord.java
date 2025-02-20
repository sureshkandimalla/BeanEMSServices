package com.bean.domain;

import java.time.LocalDate;

public record EmployeeReconcileRecord(Long employeeId, String designation, String dob, String ssn, String phoneNumber, String emailId,
                                      String employmentType, String firstName, String lastName, String gender, LocalDate startDate, LocalDate endDate,
                                      String referredBy, String taxTerms, String workAuthorization, String status, String location, Long income, Long expense,Long netProfit ) {

}
