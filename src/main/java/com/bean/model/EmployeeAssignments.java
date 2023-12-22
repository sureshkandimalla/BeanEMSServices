package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "EmployeeAssignments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeAssignments {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long employeeAssignmentID;


  @UpdateTimestamp
  private LocalDate LastUpdated;
}
