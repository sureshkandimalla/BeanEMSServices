package com.employeehub.repository;

import com.employeehub.model.Coi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoiRepository extends JpaRepository<Coi, Long> {

    List<Coi> findByVendorId(Long vendorId);

    List<Coi> findByCustomerId(Long customerId);
}
