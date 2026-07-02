package com.bean.repository;

import com.bean.model.PayrollSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PayrollSummaryRepository extends JpaRepository<PayrollSummary, Long> {
    List<PayrollSummary> findByEmployeeNameContainingIgnoreCase(String employeeName);
    List<PayrollSummary> findByCheckDateBetween(LocalDate startDate, LocalDate endDate);
    List<PayrollSummary> findByDepartment(String department);
    List<PayrollSummary> findByEmployeeId(Long employeeId);
}
