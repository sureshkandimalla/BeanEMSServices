package com.bean.repository;

import com.bean.model.LCA;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LCARepository extends JpaRepository<LCA, Long> {
    List<LCA> findByEmployee_EmployeeId(Long employeeId);
}
