package com.bean.repository;

import com.bean.model.ImmigrationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmigrationDetailsRepository extends JpaRepository<ImmigrationDetails, Long> {

    List<ImmigrationDetails> findByVisaType(String visaType);
}
