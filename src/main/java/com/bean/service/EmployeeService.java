package com.bean.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.bean.domain.BasicEmployee;
import com.bean.util.NullPropertyUtils;
import org.springframework.beans.BeanUtils;
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
import com.bean.config.CompanyIdRangeConfig;

@Service
public class EmployeeService {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private CompanyIdRangeConfig companyIdRangeConfig;

	@Autowired
    private EmployeeRepository employeeRepository;

	public Optional<Employee> saveEmployee(com.bean.domain.Employee employee) throws EmployeeServiceException {
		Employee entity = employeeRepository.findById(employee.employeeId())
				.orElseThrow(() -> new IllegalArgumentException("Employee not found"));

		// copy only non-null properties from dto into entity
		BeanUtils.copyProperties(employee, entity, NullPropertyUtils.getNullPropertyNames(employee));

		entity.setLastUpdated(LocalDate.now());

		try {
			Employee savedEmployee = employeeRepository.save(entity);
			return Optional.of(savedEmployee);
		} catch (Exception e) {
			throw new EmployeeServiceException("Employee data not saved", e);
		}
		//return employeeRepository.save(entity);
	}
		public Optional<Employee> saveEmployeeDetails(com.bean.domain.Employee employee) throws EmployeeServiceException {
		
		Employee emp = new Employee();

		// Assign ID from reserved company range if companyName is provided.
		// ID range is chosen strictly by status value:
		//   status == "newHires"  → new-hire range  (Intellan: 500-599, Code9: 600-699)
		//   any other status      → standard range  (Intellan: 1-100,   Code9: 101-200)
		// If status is anything other than "newHires" the standard range is used,
		// preventing employees like "Active" from accidentally landing in 600-699.
		if (employee.companyName() != null && !employee.companyName().isEmpty()) {
			emp.setCompanyName(employee.companyName());

			boolean isNewHire = employee.status() != null
					&& employee.status().trim().equalsIgnoreCase("newHires");

			long[] range = isNewHire
					? companyIdRangeConfig.getNewHireRangeFor(employee.companyName())
					: companyIdRangeConfig.getRangeFor(employee.companyName());

			logger.info("saveEmployeeDetails: company='{}' status='{}' isNewHire={} range=[{}-{}]",
					employee.companyName(), employee.status(), isNewHire,
					range != null ? range[0] : "n/a", range != null ? range[1] : "n/a");

			if (range != null) {
				long nextId = nextAvailableIdInRange(range[0], range[1]);
				if (nextId == -1) {
					throw new EmployeeServiceException("No available IDs left in reserved "
							+ (isNewHire ? "new-hire" : "standard")
							+ " range for company: " + employee.companyName(), null);
				}
				emp.setEmployeeId(nextId);
				logger.info("saveEmployeeDetails: assigned employeeId={}", nextId);
			}
		}
		// TODO mapping based on UI to DB
		emp.setFirstName(employee.firstName());
		emp.setLastName(employee.lastName());
		emp.setDesignation(employee.designation());
		emp.setSsn(employee.ssn());
		emp.setDob(employee.dob());
		emp.setGender(employee.gender().toUpperCase());
		emp.setEmailId(employee.emailId());
		emp.setPhone(employee.phoneNumber());
		emp.setWorkCountry(employee.country().toString());
		emp.setWorkCity(emp.getWorkCity());

		emp.setEmploymentType(employee.employmentType());

		emp.setTaxTerm(employee.taxTerms());
		emp.setVisa(employee.visa());
		emp.setStatus(employee.status());
		emp.setLocation(employee.location());

		emp.setEndDate(employee.endDate());
		emp.setStartDate(employee.startDate());
		emp.setReferredBy(employee.referredBy());
		emp.setLastUpdated(LocalDate.now());
		emp.setI9(employee.i9());
		emp.setEverifyStatus(employee.everifyStatus());
		emp.setPAF(employee.PAF());

		
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

	/**
	 * Finds the next unused ID in [start, end] by checking existing IDs in the DB.
	 * Returns -1 if the range is fully occupied.
	 */
	private long nextAvailableIdInRange(long start, long end) {
		List<Long> usedIds = employeeRepository.findUsedIdsInRange(start, end);
		Set<Long> usedSet = new java.util.HashSet<>(usedIds);
		for (long id = start; id <= end; id++) {
			if (!usedSet.contains(id)) return id;
		}
		return -1;
	}

	public Optional<List<DashboardEmployeeDetails>> getEmpListByStatus(String status) {

		List<Employee> empList = employeeRepository.getEmpListByStatus(status);
		
		List<DashboardEmployeeDetails> empDetailsList = empList.stream()
		        .map(e -> new DashboardEmployeeDetails(e.getFirstName(),e.getLastName(), e.getGender(), e.getDesignation(), e.getLocation(), e.getPrimarySkills()))
		        .collect(Collectors.toList());
		
		return Optional.of(empDetailsList);
	}
	public List<BasicEmployee> getEmployees() {
		return employeeRepository.getEmployee().stream().map(emp -> new BasicEmployee((BigInteger) emp[0], (String) emp[1], (String) emp[2])).collect(Collectors.toList());
	}

}
