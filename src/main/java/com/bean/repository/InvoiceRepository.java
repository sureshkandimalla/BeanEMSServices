package com.bean.repository;

import com.bean.model.Assignment;
import com.bean.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(
            value = "SELECT * FROM invoice a where DATE_FORMAT(a.start_date,'%Y%m')<=? and DATE_FORMAT(a.end_date,'%Y%m')>=?",
            nativeQuery = true)
    List<Invoice> findAllActiveInvoicesForTheMonth(String startDate, String endDate);
   
    @Query(value = "SELECT * FROM invoice a where DATE_FORMAT(a.invoice_month,'%Y%m') =? ",
            nativeQuery = true)
	List<Invoice> findAllInvoicesForTheMonth(String yearMonthReq);

	//void saveAll(List<com.bean.domain.Invoice> filteredInvoices);


}
