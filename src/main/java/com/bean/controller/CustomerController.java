package com.bean.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bean.exception.ResourceNotFoundException;
import com.bean.repository.CustomerRepository;
import com.bean.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class CustomerController {

  @Autowired
  private CustomerRepository customerRepository;

  @GetMapping("/customers")
  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  @PostMapping("/customers")
  public Customer createCustomer(@RequestBody Customer customer) {
    return customerRepository.save(customer);
  }

  @GetMapping("/customers/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
    Customer customer = customerRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("customer not exist with id :" + id)
      );
    return ResponseEntity.ok(customer);
  }

  @PutMapping("/customers/{id}")
  public ResponseEntity<Customer> updateCustomer(
    @PathVariable Long id,
    @RequestBody Customer customerDetails
  ) {
    Customer customer = customerRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("customer not exist with id :" + id)
      );

    customer.setCustomerName(customerDetails.getCustomerName());

    Customer updatedCustomer = customerRepository.save(customer);
    return ResponseEntity.ok(updatedCustomer);
  }

  @DeleteMapping("/customers/{id}")
  public ResponseEntity<Map<String, Boolean>> deletecustomer(
    @PathVariable Long id
  ) {
    Customer customer = customerRepository
      .findById(id)
      .orElseThrow(() ->
        new ResourceNotFoundException("customer not exist with id :" + id)
      );

    customerRepository.delete(customer);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return ResponseEntity.ok(response);
  }
}
