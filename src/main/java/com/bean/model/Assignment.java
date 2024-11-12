package com.bean.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@Entity
@Table(name = "Assignment")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Assignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long assignmentId;
  private Long projectId;
  private String assignmentType;
  private String assignmentTaxType;
  private LocalDate startDate;
  private LocalDate endDate;

  private long wage;

  private String status;
  /*@ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")*/
  private long employeeId;

  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JoinColumn(name = "assignmentId")
  private List<Notes> assignmentNotes;

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "Assignment{" +
            "assignmentId=" + assignmentId +
            ", projectId=" + projectId +
            ", assignmentType='" + assignmentType + '\'' +
            ", assignmentTaxType='" + assignmentTaxType + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", wage=" + wage +
            ", status='" + status + '\'' +
            ", employeeId=" + employeeId +
            ", assignmentNotes=" + assignmentNotes +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
