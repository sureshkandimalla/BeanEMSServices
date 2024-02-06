package com.bean.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bean.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	@Query(
            value = "SELECT * FROM project a where DATE_FORMAT(a.end_date,'%Y-%m-%d')>=?",
            nativeQuery = true)
    List<Project> findAllActiveProjectsByDate(String startDate, String endDate);
   
}