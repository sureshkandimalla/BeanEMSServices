package com.bean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bean.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	@Query(
            value = "SELECT * FROM project a WHERE DATE_FORMAT(a.start_date, '%Y-%m') <= :endDate AND DATE_FORMAT(a.end_date, '%Y-%m') >= :endDate",
            nativeQuery = true)
    List<Project> findAllActiveProjectsForMonth(String endDate);
    @Query(
            value = "SELECT * FROM project a where DATE_FORMAT(a.end_date,'%Y-%m-%d')>=?",
            nativeQuery = true)
    List<Project> findAllActiveProjectsByDate(String endDate);
    @Query(
            value = "SELECT * FROM project a where a.employee_id=?",
            nativeQuery = true)
    List<Project> findAllProjectsByEmployee(long employeeId);
   
}