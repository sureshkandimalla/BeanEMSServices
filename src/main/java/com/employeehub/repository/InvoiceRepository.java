package com.employeehub.repository;

import com.employeehub.model.Assignment;
import com.employeehub.model.Invoice;
import com.employeehub.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
    @Query(
            value = "SELECT * FROM invoice a where DATE_FORMAT(a.start_date,'%Y%m')<=? and DATE_FORMAT(a.end_date,'%Y%m')>=?",
            nativeQuery = true)
    List<Invoice> findAllActiveInvoicesForTheMonth(String startDate, String endDate);
    @Query(
            value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m')<=:endDate and DATE_FORMAT(a.invoice_month,'%Y%m')>=:startDate and a.project_id =:projectId ",
            nativeQuery = true)
    List<Invoice> findAllInvoicesForTheMonthProject(String startDate, String endDate,long projectId);
   
    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? ",
            nativeQuery = true)
    List<Invoice> findAllInvoicesForTheMonth(String yearMonthReq);

    @Query(value = "SELECT * FROM invoice ",
            nativeQuery = true)
    List<Invoice> getAllInvoices();

    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? and a.project_id =?", nativeQuery = true)
	List<Invoice> findByInvoiceByMonthAndProjectId(String selectedDate, Long projectId);

    @Query(value = "SELECT * FROM invoice  where project_id in( SELECT project_id FROM project where employee_id =?)",
            nativeQuery = true)
    List<Invoice> findByEmployee(long employeeId);

    // Project's customerId is physically stored in its vendor_id column —
    // see ProjectRepository#findAllProjectsByCustomer.
    @Query(value = "SELECT * FROM invoice where project_id in (SELECT project_id FROM project where vendor_id =?)",
            nativeQuery = true)
    List<Invoice> findByCustomer(long customerId);
    // Replaces the old findByInvoiceByMonthAndInvoiceId: "does an invoice
    // already exist for this project/month" must be keyed off the actual
    // business identity (one invoice per project per month), never off a
    // user-typed number — that's what let two unrelated invoices collide
    // and overwrite each other. See InvoiceService#createInvoiceObject.
    //
    // DO NOT USE for new code — kept only for reference/compat. Matching on
    // calendar month (DATE_FORMAT('%Y%m')) is wrong for every invoice term
    // except Monthly: Weekly/Biweekly/Once-in-4-Weeks/Semi-Monthly all put
    // more than one legitimate invoice in the same calendar month, so this
    // either throws NonUniqueResultException (2+ rows already in the month)
    // or silently overwrites a *different* period's invoice in place (found
    // exactly 1 row, which happened to be some other week) — confirmed live
    // in prod: two distinct weekly invoices for Chandra-Aquinas collided
    // into one row, one save 500'd, and the surviving row was left with the
    // wrong start/end dates because the update path never touches them.
    // findByProjectIdAndStartDate below is the real fix — one invoice per
    // *exact period*, which is unique for every term including Monthly.
    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? and a.project_id =?", nativeQuery = true)
	Optional<Invoice> findByProjectIdAndInvoiceMonth(String invoiceMonth, Long projectId);

    // The actual business identity: one invoice per project per exact
    // period (its start date) — correct for every invoice term, since
    // start_date is unique per period even when several periods share a
    // calendar month (Weekly/Biweekly/Once-in-4-Weeks/Semi-Monthly) or
    // coincide with the 1st of the month (Monthly).
    List<Invoice> findByProjectIdAndStartDate(Long projectId, LocalDate startDate);

    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? and a.status =?", nativeQuery = true)
    List<Invoice> findAllInvoicesForTheMonthAndStatus(String formattedDate, String status);
    
    @Query(value = "SELECT status, COUNT(*) as count FROM invoice WHERE status IN ('paid', 'pending', 'upcoming', 'overdew') GROUP BY status", nativeQuery = true)
	List<Map<String, String>> getInvoiceCountByStatus();
    List<Invoice> findByProjectId(Long projectId);


	//void saveAll(List<com.employeehub.domain.Invoice> filteredInvoices);


}
