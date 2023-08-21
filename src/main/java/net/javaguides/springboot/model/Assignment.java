package net.javaguides.springboot.model;

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
  private Long id;

  private String assignmentType;
  private LocalDate startDate;
  private LocalDate endDate;
  private double billRate;
  private String status;
  private String note;



  @Override
  public String toString() {
    return "{" +
      " id='" + getId() + "'" +
      ", assignmentType='" + getAssignmentType() + "'" +
      ", startDate='" + getStartDate() + "'" +
      ", endDate='" + getEndDate() + "'" +
      ", billRate='" + getBillRate() + "'" +
      ", status='" + getStatus() + "'" +
      ", note='" + getNote() + "'" +
      "}";
  }


  
  
}
