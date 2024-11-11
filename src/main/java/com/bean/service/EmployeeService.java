package com.bean.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.model.Customer;
import com.bean.model.Employee;
import com.bean.domain.DashboardEmployeeDetails;
import com.bean.exception.DataNotSavedException;
import com.bean.exception.EmployeeServiceException;
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
	
	public Optional<Employee> saveEmployeeDetails(com.bean.domain.Employee employee) throws EmployeeServiceException {
		
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
		emp.setPhone(employee.phoneNumber());
		emp.setReferredBy(employee.referredBy());
		emp.setSsn(employee.ssn());
		emp.setTaxTerm(employee.taxTerms());
		emp.setVisa(employee.workAuthorization());
		emp.setStatus(employee.status());
		emp.setLocation(employee.location());
		emp.setLastUpdated(LocalDate.now());
		
		Address address = new Address();
		address.setAddress(employee.streetAddress());
		address.setCity(employee.city());
		address.setState(employee.state());
		address.setCountry(employee.country());
		address.setZipCode(employee.zipCode());
		Optional.ofNullable(emp.getAddress()).orElseGet(() -> { emp.setAddress(new ArrayList<>()); return emp.getAddress(); }).add(address);


		//emp.setAddress(address);
        address.setEmployee(emp);
		
        try {
            Employee savedEmployee = employeeRepository.save(emp);
            return Optional.of(savedEmployee);
        } catch (Exception e) {
            throw new EmployeeServiceException("Employee data not saved", e);
        }
	}

	public Optional<List<DashboardEmployeeDetails>> getEmpListByStatus(String status) {

		List<Employee> empList = employeeRepository.getEmpListByStatus(status);
		
		List<DashboardEmployeeDetails> empDetailsList = empList.stream()
		        .map(e -> new DashboardEmployeeDetails(e.getFirstName(),e.getLastName(), e.getGender(), e.getDesignation(), e.getLocation(), e.getPrimarySkills()))
		        .collect(Collectors.toList());
		
		return Optional.of(empDetailsList);
	}

}
