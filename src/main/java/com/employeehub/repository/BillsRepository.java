package com.employeehub.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.employeehub.model.Bills;

public interface BillsRepository extends JpaRepository<Bills, Long> {
	
	@Query(
            value = "SELECT * FROM bills a where DATE_FORMAT(a.invoice_Month,'%Y%m')=?",
            nativeQuery = true)
    Optional<List<Bills>> findBillsByInvoiceMonth(String invoiceMonth);
    // Filters bills.project_id directly — previously joined through
    // invoice.project_id, which leaked another project's bills into the
    // result whenever an invoice's project_id was wrong (see the
    // invoice-surrogate-key migration this bug motivated).
    @Query(
            value = "SELECT * FROM bills where project_id=:projectId",
            nativeQuery = true)
    Optional<List<Bills>> findBillsForProject(long projectId);
   Optional<List<Bills>>  findByEmployeeId(long employeeId);

   @Query(value = "SELECT DISTINCT invoice_id FROM bills", nativeQuery = true)
   List<Long> findDistinctInvoiceIdsWithBills();

   List<Bills> findByInvoiceId(Long invoiceId);

   List<Bills> findByProjectId(Long projectId);

   // Project's own customerId is physically stored in its vendor_id column
   // — see ProjectRepository#findAllProjectsByCustomer.
   @Query(value = "SELECT * FROM bills where project_id in (SELECT project_id FROM project where vendor_id=?)",
           nativeQuery = true)
   List<Bills> findByCustomer(long customerId);

   // Any bill whose own [startDate, endDate] billing window overlaps the
   // given range at all — standard interval-overlap condition
   // (start <= rangeEnd AND end >= rangeStart). Bills with a null start/end
   // (never should happen post-invoice-creation, but defensively excluded
   // by the NOT NULL checks) are left out since there's no window to
   // compare against.
   @Query("SELECT b FROM Bills b WHERE b.startDate IS NOT NULL AND b.endDate IS NOT NULL " +
          "AND b.startDate <= :rangeEnd AND b.endDate >= :rangeStart")
   List<Bills> findOverlappingRange(@Param("rangeStart") LocalDate rangeStart, @Param("rangeEnd") LocalDate rangeEnd);

}
