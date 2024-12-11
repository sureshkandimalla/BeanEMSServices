package com.bean.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bean.model.Bills;
import com.bean.model.Invoice;
import com.bean.repository.BillsRepository;

@Service
public class BillsService {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BillsService.class);
	
	@Autowired
    private BillsRepository billsRepository;
	
	public Optional<List<Bills>> findBillsByInvoiceMonth(String formattedDate) {
		
		Optional<List<Bills>> existingInvoice = billsRepository.findBillsByInvoiceMonth(formattedDate);
		
		// TODO mapping based on UI display
		return existingInvoice;
	}
	public Optional<List<Bills>> findBillsForProject(Long projectId) {

		Optional<List<Bills>> bills = billsRepository.findBillsForProject(projectId);

		// TODO mapping based on UI display
		return bills;
	}
	public Optional<List<Bills>> findBillsForEmployee(Long employeeId) {

		Optional<List<Bills>> bills = billsRepository.findByEmployeeId(employeeId);

		// TODO mapping based on UI display
		return bills;
	}

}
