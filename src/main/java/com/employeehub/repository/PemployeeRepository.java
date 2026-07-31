package com.employeehub.repository;

import com.employeehub.model.PotentialEmployee;
import com.employeehub.model.Wage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PemployeeRepository extends JpaRepository<PotentialEmployee, Long> {


}
