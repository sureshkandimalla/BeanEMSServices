package com.employeehub.repository;

import com.employeehub.model.PotentialEmployee;
import com.employeehub.model.Wage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PemployeeRepository extends JpaRepository<PotentialEmployee, Long> {

    // PotentialEmployee.peId is a manually-assigned primitive `long` (no
    // @GeneratedValue) — Hibernate's default unsaved-value heuristic for a
    // primitive id treats any entity instance with id == 0 as transient,
    // so the inherited deleteById(0) silently no-ops instead of deleting.
    // A direct bulk-delete query sidesteps entity-state tracking entirely
    // and works for every id, including 0.
    @Modifying
    @Query("DELETE FROM PotentialEmployee p WHERE p.peId = :id")
    void deleteByPeId(Long id);
}
