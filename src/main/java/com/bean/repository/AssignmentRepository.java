package com.bean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bean.model.Assignment;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query(
            value = "SELECT * FROM assignment a where DATE_FORMAT(a.start_date,'%Y%m')<=? and DATE_FORMAT(a.end_date,'%Y%m')>=?",
            nativeQuery = true)
    List<Assignment> findAllActiveAssignment(String startDate,String endDate);

    @Query(
            value = "SELECT * FROM assignment a where a.project_id =? ",
            nativeQuery = true)
	List<Assignment> findByProjectId(Long projectId);
}
