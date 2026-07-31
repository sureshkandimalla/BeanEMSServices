package com.employeehub.service;

import com.employeehub.domain.EmployeeReconcileRecord;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeReconcileRecordMapper {

    public static EmployeeReconcileRecord mapToReconcileRecord(com.employeehub.model.Employee modelEmployee, Long income, Long expense) {
        return new EmployeeReconcileRecord(
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
                modelEmployee.getStatus(),
                //set Address here
                null,
                income,expense,income-expense
        );
    }

    // Map from a list of models to a list of domains
    /*public static List<com.employeehub.domain.Employee> mapToDomainList(List<com.employeehub.model.Employee> modelEmployees) {
        return modelEmployees.stream()
                .map(EmployeeMapper::mapToDomain)
                .collect(Collectors.toList());
    }*/
}
