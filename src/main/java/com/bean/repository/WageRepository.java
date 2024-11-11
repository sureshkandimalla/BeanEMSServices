package com.bean.repository;

import com.bean.model.Assignment;
import com.bean.model.Wage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WageRepository extends JpaRepository<Wage, Long> {


}
