package com.bean.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bean.exception.BillsException;
import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Bills;
import com.bean.model.Invoice;
import com.bean.repository.BillsRepository;

@Service
public class BillsService {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BillsService.class);

	@Autowired
    private BillsRepository billsRepository;

	public Bills getBillById(Long id) {
		return billsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Bill not exist with id: " + id));
	}

	public Bills createBill(Bills bill) throws BillsException {
		try {
			return billsRepository.save(bill);
		} catch (Exception e) {
			throw new BillsException("Bill data not saved", e);
		}
	}

	public Bills updateBill(Long id, Bills billDetails) throws BillsException {
		Bills existingBill = getBillById(id);

		switch (billDetails.getStatus() == null ? "" : billDetails.getStatus().toLowerCase()) {
			case "paid":
				if (billDetails.getBillPaidAmount() == 0) {
					billDetails.setBillPaidAmount(billDetails.getTotal());
				}
				break;
			case "unpaid":
				if (billDetails.getBillPaidAmount() != 0) {
					billDetails.setBillPaidAmount(0);
				}
				break;
			default:
				break;
		}

		billDetails.setBillId(existingBill.getBillId());
		try {
			return billsRepository.saveAndFlush(billDetails);
		} catch (Exception e) {
			throw new BillsException("Bill data not saved", e);
		}
	}

	public void deleteBill(Long id) {
		Bills existingBill = getBillById(id);
		billsRepository.delete(existingBill);
	}

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
