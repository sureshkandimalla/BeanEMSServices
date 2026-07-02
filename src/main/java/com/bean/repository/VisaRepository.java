package com.bean.repository;

import com.bean.model.Visa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Long> {

    List<Visa> findByEmployee_EmployeeId(Long employeeId);

    Optional<Visa> findFirstByEmployee_EmployeeIdOrderByStartDateDesc(Long employeeId);

    @Query("SELECT v FROM Visa v WHERE v.visaId = :id")
    Optional<Visa> findByVisaId(@Param("id") long id);
}
