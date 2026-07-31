package com.employeehub.repository;

import com.employeehub.model.Insurance;
import com.employeehub.model.Wage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
    List<Insurance> findByEmployeeId(long employeeId);



}
