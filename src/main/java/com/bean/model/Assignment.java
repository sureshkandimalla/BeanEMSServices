package com.bean.model;

import java.time.LocalDate;
import javax.persistence.*;
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

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_id")
  private Employee employee;

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
