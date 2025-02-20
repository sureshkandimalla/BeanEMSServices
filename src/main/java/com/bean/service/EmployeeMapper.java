package com.bean.service;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {
    public static com.bean.model.Employee mapToModel(com.bean.domain.Employee domainEmployee) {
        com.bean.model.Employee modelEmployee = new com.bean.model.Employee();

        modelEmployee.setEmployeeId(domainEmployee.employeeId());
        modelEmployee.setFirstName(domainEmployee.firstName());
        modelEmployee.setLastName(domainEmployee.lastName());
        modelEmployee.setEmailId(domainEmployee.emailId());
        modelEmployee.setPhone(domainEmployee.phoneNumber());
        modelEmployee.setDob(domainEmployee.dob());
        modelEmployee.setStartDate(domainEmployee.startDate());
        modelEmployee.setEndDate(domainEmployee.endDate());
        modelEmployee.setDesignation(domainEmployee.designation());
        modelEmployee.setEmploymentType(domainEmployee.employmentType());
        modelEmployee.setStatus(domainEmployee.status());
        modelEmployee.setLocation(domainEmployee.location());
        // Map additional fields if necessary

        return modelEmployee;
    }

    public static List<com.bean.model.Employee> mapToModelList(List<com.bean.domain.Employee> domainEmployees) {
        return domainEmployees.stream()
                .map(EmployeeMapper::mapToModel)
                .collect(Collectors.toList());
    }
    public static com.bean.domain.Employee mapToDomain(com.bean.model.Employee modelEmployee, Long income, Long expense) {
        return new com.bean.domain.Employee(
                modelEmployee.getEmployeeId(),
                modelEmployee.getDesignation(),
                modelEmployee.getDob(),
                modelEmployee.getSsn(),
                modelEmployee.getPhone(),
                modelEmployee.getEmailId(),
                modelEmployee.getEmploymentType(),
                modelEmployee.getFirstName(),
                modelEmployee.getLastName(),
                modelEmployee.getGender(),
                modelEmployee.getStartDate(),
                modelEmployee.getEndDate(),
                modelEmployee.getReferredBy(),
                modelEmployee.getTaxTerm(),
                modelEmployee.getVisa(),
                //set Address here
                null,null,null,null,null,null,
                modelEmployee.getStatus(),
                modelEmployee.getLocation(),
                income,expense
        );
    }

    // Map from a list of models to a list of domains
    /*public static List<com.bean.domain.Employee> mapToDomainList(List<com.bean.model.Employee> modelEmployees) {
        return modelEmployees.stream()
                .map(EmployeeMapper::mapToDomain)
                .collect(Collectors.toList());
    }*/
}
