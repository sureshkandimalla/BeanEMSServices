package com.bean.repository;

import com.bean.model.Assignment;
import com.bean.model.Invoice;
import com.bean.model.Payroll;
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
    @Query(value = "SELECT * FROM invoice  where project_id in( SELECT project_id FROM project where employee_id =?)",
            nativeQuery = true)
    List<Invoice> findByEmployee(long employeeId);

    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? and a.project_id =?", nativeQuery = true)
	List<Invoice> findByInvoiceByMonthAndProjectId(String selectedDate, Long projectId);
    
    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? and a.invoice_Id =?", nativeQuery = true)
	Optional<Invoice> findByInvoiceByMonthAndInvoiceId(String invoiceMonth, Long invoiceId);

    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? and a.status =?", nativeQuery = true)
    List<Invoice> findAllInvoicesForTheMonthAndStatus(String formattedDate, String status);
    
    @Query(value = "SELECT status, COUNT(*) as count FROM invoice WHERE status IN ('paid', 'pending', 'upcoming', 'overdew') GROUP BY status", nativeQuery = true)
	List<Map<String, String>> getInvoiceCountByStatus();
   Optional<Invoice> findByInvoiceId(Long employeeId);
    Optional<List<Invoice>> findByProjectId(Long projectId);

	//void saveAll(List<com.bean.domain.Invoice> filteredInvoices);


}
