package com.bean.controller;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bean.domain.BasicEmployee;
import com.bean.repository.EmployeeRepository;
import com.bean.service.CustomerService;
import com.bean.service.EmployeeService;
import com.bean.domain.DashboardEmployeeDetails;
import com.bean.exception.EmployeeServiceException;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Customer;
import com.bean.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4000", "http://localhost:3001" })
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeService employeeService;

	/*
	 * get all employees
	 */

	@GetMapping("/getAllEmployees")
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}
	@GetMapping("/getEmployees")
	public List<BasicEmployee> getEmployees() {
		return employeeRepository.getEmployee().stream().map(emp -> new BasicEmployee((BigInteger) emp[0], (String) emp[1], (String) emp[2])).collect(Collectors.toList());
	}

	// create employee rest api
	@PostMapping("/saveOnBoardDetails")
	public ResponseEntity<Optional<Employee>> createEmployee(@RequestBody com.bean.domain.Employee employee)
			throws EmployeeServiceException {

		logger.info("employee:: " + employee.toString());
		Optional<Employee> respEmployee = employeeService.saveEmployeeDetails(employee);

		logger.info("respEmployee:: " + respEmployee.toString());

		return ResponseEntity.ok(respEmployee);

	}

	/*
	 * get all employees count by status returns key-value of all emp with status
	 */

	@GetMapping("/employeesCountByStatus")
	public List<Map<String, String>> getEmployeesCountByStatus() {

		List<Map<String, String>> empStatus = employeeRepository.getEmpCountByStatus();
		logger.info("empStatus:: " + empStatus.toString());
		return empStatus;
	}

	/*
	 * get all employees List by status returns employees List with name, role etc
	 */

	@GetMapping("/employeesListByStatus")
	public ResponseEntity getEmployeesListByStatus(@RequestParam(required = true) String status) {
		logger.info("status:: "+status);
		
		if (status == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Status cannot be null");
		}

		Optional<List<DashboardEmployeeDetails>> empByStatusList = employeeService.getEmpListByStatus(status);
		
		logger.info("empByStatusList:: " + empByStatusList.toString());
		return ResponseEntity.of(empByStatusList);
	}

	// get employee by id rest api
	@GetMapping("/employee/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));
		return ResponseEntity.ok(employee);
	}

	// update employee rest api

	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setEmailId(employeeDetails.getEmailId());

		Employee updatedEmployee = employeeRepository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
	}

	// delete employee rest api
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id :" + id));

		employeeRepository.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
}
