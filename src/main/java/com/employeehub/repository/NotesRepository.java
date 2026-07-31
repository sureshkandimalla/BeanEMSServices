package com.employeehub.repository;

import com.employeehub.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByEmployee_EmployeeId(Long employeeId);
}
