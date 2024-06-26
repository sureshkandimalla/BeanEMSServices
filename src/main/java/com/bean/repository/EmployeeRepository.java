package com.bean.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bean.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	@Query(value = "SELECT status, COUNT(*) as count FROM employees WHERE status IN ('onBoarding', 'active', 'NewHires', 'bench') GROUP BY status",
            nativeQuery = true)
	List<Map<String, String>> getEmpCountByStatus();
	
	
	@Query(value = "SELECT * FROM employees WHERE status = ?",
            nativeQuery = true)
	List<Employee> getEmpListByStatus(String status);

}
