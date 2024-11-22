package com.bean.repository;

import com.bean.model.Adjustment;
import com.bean.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
    @Query(
            value = "SELECT * FROM bills a where DATE_FORMAT(a.invoice_Month,'%Y%m')=?",
            nativeQuery = true)
    List<Adjustment> findByFromId(Long employeeId);

    /*@Query("SELECT a FROM Adjustment a " +
            "LEFT JOIN FETCH./ a.fromEmployee eFrom " +
            "LEFT JOIN FETCH a.toEmployee eTo " +
            "WHERE a.fromId = :id OR a.toId = :id")*/
    @Query(value="SELECT a.adjustment_id as adjustment_id,a.last_updated as last_updated, a.adjustment_date as adjustment_date, " +
            "a.adjustment_type as adjustment_type,a.amount as amount,a.notes as notes,a.from_id as from_id,a.to_id as to_id," +
            "CONCAT(e_from.first_name, e_from.last_name) AS from_name, " +
            "CONCAT(e_to.first_name, e_to.last_name) AS to_name " +
            "FROM adjustments a " +
            "LEFT JOIN employees e_from ON a.from_id = e_from.employee_id " +
            "LEFT JOIN employees e_to ON a.to_id = e_to.employee_id " +
            "WHERE (a.from_id = :id OR a.to_id = :id)", nativeQuery = true)
    List<Object[]> findAdjustmentsByEmployeeId(@Param("id") Long id);
}