package com.bean.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

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

   @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "assignmentId")
  private List<Wage> assignmentWage;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "projectId")
  private Project project;
  private String status;

  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JoinColumn(name = "assignmentId")
  private List<Notes> assignmentNotes;

  @Override
  public String toString() {
    return "Assignment{" +
            "assignmentId=" + assignmentId +
            ", assignmentType='" + assignmentType + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", assignmentWage=" + assignmentWage +
            ", status='" + status + '\'' +
            ", assignmentNotes=" + assignmentNotes +
            '}';
  }
  @UpdateTimestamp
  private LocalDate LastUpdated;
}
