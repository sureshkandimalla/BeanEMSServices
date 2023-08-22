package com.bean.model;

import java.time.LocalDate;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Assignment")
public class Assignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long assignmentId;

  private String assignmentType;
  private LocalDate startDate;
  private LocalDate endDate;
  private double billRate;
  private String status;
  private String note;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE, optional = false)
  @JoinColumn(name = "employee_id", nullable = false)
  //@JsonIgnore
  private Employee employee;
  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE, optional = false)
  @JoinColumn(name = "project_id", nullable = false)
  //@JsonIgnore
  private Project project;

  @Override
  public String toString() {
    return "{" +
      " id='" + getAssignmentId() + "'" +
      ", assignmentType='" + getAssignmentType() + "'" +
      ", startDate='" + getStartDate() + "'" +
      ", endDate='" + getEndDate() + "'" +
      ", billRate='" + getBillRate() + "'" +
      ", status='" + getStatus() + "'" +
      ", note='" + getNote() + "'" +
      "}";
  }


  
  
}
