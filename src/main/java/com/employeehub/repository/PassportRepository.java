package com.employeehub.repository;

import com.employeehub.model.Passport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {
    List<Passport> findByEmployee_EmployeeId(Long employeeId);
}
