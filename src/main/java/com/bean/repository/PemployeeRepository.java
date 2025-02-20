package com.bean.repository;

import com.bean.model.PotentialEmployee;
import com.bean.model.Wage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PemployeeRepository extends JpaRepository<PotentialEmployee, Long> {


}
