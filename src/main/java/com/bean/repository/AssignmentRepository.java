package com.bean.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bean.model.Assignment;

import java.util.List;
import java.util.Optional;

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
    
    @Query(
            value = "SELECT * FROM assignment a where DATE_FORMAT(a.end_date,'%Y-%m-%d')>=?",
            nativeQuery = true)
    Optional<List<Assignment>> findActiveAssignmentsByEndDate(String endDate);
    
    @Query(value = "SELECT SUM(wage) FROM assignment WHERE project_id IN :projectIds", nativeQuery = true)
    double getTotalWageByProjectIds(@Param("projectIds") List<Long> projectIds);

    @Query(value =" select e.first_name as firstName,e.last_name as lastName,assignment_id as assignmentId,wage as wage, "+
            "assignment_type as assignmentType, a.status as status, a.start_date as startDate,a.end_date as endDate,"+
            "a.last_updated as lastUpdatedDate from assignment a,employees e "+
            "where a.employee_id=e.employee_id and a.project_id=?",
            nativeQuery = true)
    List<Object[]> findAssignmentsForProject(Long projectId);
    
}
