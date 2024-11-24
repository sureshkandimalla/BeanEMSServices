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
		cust.setCustomerAddress("ADDRESS");
		return Optional.of(customerRepository.save(cust));

		//return cust;
	}

}
