package com.bean.repository;

import com.bean.model.Bills;
import com.bean.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    List<Payroll> findByEmployeeId(Long employeeId);
    @Query("SELECT new com.bean.model.Payroll(p.payrollId, p.payCheckId, p.employeeId, pp.startDate, pp.payDate, pp.endDate, p.hours, p.totalPaid, p.taxWithheld, p.deductions, p.netPay, p.employerLiability, p.notes, p.LastUpdated) " +
            "FROM Payroll p JOIN PayPeriod pp ON p.payCheckId = pp.payPeriodId " +
            "WHERE p.employeeId = :employeeId")
    List<Payroll> findPayrollsWithPayPeriodDatesByEmployeeId(Long employeeId);
}

