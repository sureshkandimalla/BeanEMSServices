package com.employeehub.repository;

import com.employeehub.model.ImmiIntake;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImmiIntakeRepository extends JpaRepository<ImmiIntake, Long> {
    List<ImmiIntake> findByEmployeeId(Long employeeId);
}
