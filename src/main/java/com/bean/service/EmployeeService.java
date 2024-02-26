package com.bean.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.model.Customer;
import com.bean.model.Employee;
import com.bean.model.Address;
import com.bean.model.Bills;
import com.bean.repository.BillsRepository;
import com.bean.repository.CustomerRepository;
import com.bean.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmployeeService.class);
	
	@Autowired
    private EmployeeRepository employeeRepository;
	
	public Optional<Employee> saveEmployeeDetails(com.bean.domain.Employee employee) {
		
		Employee emp = new Employee();
		// TODO mapping based on UI to DB
		emp.setDesignation(employee.designation());
		emp.setDob(employee.dob());
		emp.setEmailId(employee.emailId());
		emp.setEmploymentType(employee.employmentType());
		emp.setEndDate(employee.endDate());
		emp.setStartDate(employee.startDate());
		emp.setFirstName(employee.firstName());
		emp.setLastName(employee.lastName());
		emp.setGender(employee.gender());
		emp.setPhone(employee.phone());
		emp.setReferredBy(employee.referredBy());
		emp.setSsn(employee.ssn());
		emp.setTaxTerm(employee.taxTerm());
		emp.setVisa(employee.visa());
		emp.setLastUpdated(LocalDate.now());
		emp.setTaxTerm(employee.taxTerm());
		
		Address address = new Address();
		address.setAddress(employee.address_line_1() +" "+ employee.address_line_2());
		address.setCity(employee.city());
		address.setState(employee.state());
		address.setCountry(employee.country());
		address.setZipCode(employee.zipCode());
		
		emp.setAddress(address);
        address.setEmployee(emp);
		
		return Optional.of(employeeRepository.save(emp));
	}

}
