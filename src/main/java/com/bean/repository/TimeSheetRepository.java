package com.bean.repository;

import com.bean.model.TimeSheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
    List<TimeSheet> findByAssignmentIdAndWorkDateBetweenOrderByWorkDate(Long assignmentId, LocalDate start, LocalDate end);
    List<TimeSheet> findByEmployeeIdAndWorkDateBetweenOrderByWorkDate(Long employeeId, LocalDate start, LocalDate end);
    List<TimeSheet> findByWorkDateBetweenOrderByWorkDate(LocalDate start, LocalDate end);
    Optional<TimeSheet> findByAssignmentIdAndWorkDate(Long assignmentId, LocalDate workDate);
}
