package com.bean.repository;

import com.bean.model.Assignment;
import com.bean.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
    @Query(
            value = "SELECT * FROM invoice a where DATE_FORMAT(a.start_date,'%Y%m')<=? and DATE_FORMAT(a.end_date,'%Y%m')>=?",
            nativeQuery = true)
    List<Invoice> findAllActiveInvoicesForTheMonth(String startDate, String endDate);
   
    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? ",
            nativeQuery = true)
	List<Invoice> findAllInvoicesForTheMonth(String yearMonthReq);

    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? and a.project_id =?",
            nativeQuery = true)
	List<Invoice> findByInvoiceByMonthAndProjectId(String selectedDate, Long projectId);
    
    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? and a.invoice_Id =?",
            nativeQuery = true)
	Optional<Invoice> findByInvoiceByMonthAndInvoiceId(String invoiceMonth, Long invoiceId);

	//void saveAll(List<com.bean.domain.Invoice> filteredInvoices);


}
