package com.bean.repository;

import com.bean.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByEmployeeId(Long employeeId);

    @Query(value = "SELECT status, COUNT(*) as count FROM expenses GROUP BY status", nativeQuery = true)
    List<Map<String, String>> getExpenseCountByStatus();
}
