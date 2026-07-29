package com.bean.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bean.model.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

    Optional<Customer> findByCustomerCompanyNameIgnoreCase(String customerCompanyName);
}