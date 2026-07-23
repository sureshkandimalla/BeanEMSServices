package com.bean.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bean.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	@Query("SELECT e FROM Employee e ORDER BY e.employeeId ASC")
	List<Employee> findAllSorted();

	//@Query(value = "SELECT status, COUNT(*) as count FROM employees WHERE status IN ('onBoarding', 'Terminated','Inactive','Approved','active', 'NewHires', 'bench') GROUP BY status",
			@Query(value = "SELECT status, COUNT(*) as count FROM employees  GROUP BY status",
					nativeQuery = true)
	List<Map<String, String>> getEmpCountByStatus();
	
	
	@Query(value = "SELECT * FROM employees WHERE status = ? ORDER BY employee_id",
            nativeQuery = true)
	List<Employee> getEmpListByStatus(String status);

	@Query(value = "SELECT employee_id, CONCAT(first_name,' ', last_name) AS employee_name, status FROM employees ORDER BY employee_id",
			nativeQuery = true)
	List<Object[]> getEmployee();

	@Query(value = "SELECT * FROM employees WHERE visa = ?1 ORDER BY employee_id", nativeQuery = true)
	List<Employee> getEmployeesByVisa(String visaType);

	Optional<Employee> findByLastNameIgnoreCaseAndFirstNameIgnoreCase(String lastName, String firstName);

	Optional<Employee> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

	List<Employee> findByLastNameIgnoreCase(String lastName);

	@Query(value = "SELECT employee_id FROM employees WHERE employee_id BETWEEN ?1 AND ?2 ORDER BY employee_id", nativeQuery = true)
	List<Long> findUsedIdsInRange(long start, long end);

	@Query(value = "SELECT COALESCE(MAX(employee_id), 0) FROM employees", nativeQuery = true)
	long findMaxEmployeeId();
}
