package com.bean.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bean.model.Bills;

public interface BillsRepository extends JpaRepository<Bills, Long> {
	
	@Query(
            value = "SELECT * FROM bills a where DATE_FORMAT(a.invoice_Month,'%Y%m')=?",
            nativeQuery = true)
    Optional<List<Bills>> findBillsByInvoiceMonth(String invoiceMonth);
    @Query(
            value = "SELECT * FROM bills where invoice_id in (select invoice_id FROM invoice  where project_id=:projectId)",
            nativeQuery = true)
    Optional<List<Bills>> findBillsForProject(long projectId);
   
}
