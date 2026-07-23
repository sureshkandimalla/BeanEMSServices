package com.bean.repository;

import com.bean.model.HealthInsurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, Long> {
    List<HealthInsurance> findByEmployeeNameContainingIgnoreCase(String employeeName);
    List<HealthInsurance> findByDateOfBillBetween(LocalDate startDate, LocalDate endDate);
    List<HealthInsurance> findByEmployeeId(Long employeeId);
    List<HealthInsurance> findByGroupId(String groupId);
}
