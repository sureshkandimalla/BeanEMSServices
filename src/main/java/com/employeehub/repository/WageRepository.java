package com.employeehub.repository;

import com.employeehub.model.Assignment;
import com.employeehub.model.Wage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WageRepository extends JpaRepository<Wage, Long> {


}
