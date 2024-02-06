package com.bean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bean.model.Bills;

public interface BillsRepository extends JpaRepository<Bills, Long> {
	
	@Query(
            value = "SELECT * FROM bills a where DATE_FORMAT(a.bill_date,'%Y-%m-%d')>=?",
            nativeQuery = true)
    List<Bills> findBillsByDate(String billDate);
   
}
