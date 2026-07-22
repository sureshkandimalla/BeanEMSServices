package com.bean.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.model.Customer;
import com.bean.model.Bills;
import com.bean.repository.BillsRepository;
import com.bean.repository.CustomerRepository;

@Service
public class CustomerService {
	
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CustomerService.class);
	
	@Autowired
    private CustomerRepository customerRepository;
	
	public Optional<Customer> saveCustomer(com.bean.domain.Customer customer) {
		
		Customer cust = new Customer();
		// TODO mapping based on UI to DB
		
		cust.setCustomerCompanyName(customer.vendorCompanyName());
		cust.setCustomerEmail(customer.emailId());
		cust.setCustomerName(customer.vendorName());
		cust.setCustomerPhone(customer.phone());
		cust.setCustomerStartDate(customer.startDate());
		cust.setCustomerEndDate(customer.endDate());
		cust.setCustomerStatus("Active");
		cust.setEin(customer.ein());
		cust.setWebsite(customer.webSite());
		cust.setLastUpdated(LocalDate.now());
		cust.setCustomerContactEmail(customer.emailId());
		cust.setCustomerType("Vendor");
		cust.setCustomerAddress(formatAddress(customer));
		return Optional.of(customerRepository.save(cust));

		//return cust;
	}

	// Builds a single "street, street2, city, state zip" display string from
	// the onboarding form's separate address fields — customerAddress on the
	// Customer entity is just one String column, not a structured address.
	// Blank/missing components (e.g. no street2) are simply omitted rather
	// than leaving a stray ", ".
	private String formatAddress(com.bean.domain.Customer customer) {
		StringBuilder sb = new StringBuilder();
		appendPart(sb, customer.streetAddress());
		appendPart(sb, customer.streetAddress2());
		appendPart(sb, customer.city());
		String stateZip = joinWithSpace(customer.state(), customer.zipCode());
		appendPart(sb, stateZip);
		return sb.length() > 0 ? sb.toString() : "";
	}

	private void appendPart(StringBuilder sb, String part) {
		if (part == null || part.isBlank()) return;
		if (sb.length() > 0) sb.append(", ");
		sb.append(part.trim());
	}

	private String joinWithSpace(String a, String b) {
		boolean hasA = a != null && !a.isBlank();
		boolean hasB = b != null && !b.isBlank();
		if (hasA && hasB) return a.trim() + " " + b.trim();
		if (hasA) return a.trim();
		if (hasB) return b.trim();
		return null;
	}

}
