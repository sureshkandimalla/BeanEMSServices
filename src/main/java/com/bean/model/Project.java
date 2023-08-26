package com.bean.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@Entity
@Table(name = "Project")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long projectId;

  private String projectName;

  @OneToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "projectId")
  private Vendor vendor;
  private String client;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "projectId")
  private List<Wage> billRate;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;
  private String invoiceTerm;
  private String paymentTerm;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "projectId")
  private List<Notes> projectNotes;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "projectId")
  private List<Assignment> assignments;

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "Project{" +
            "projectId=" + projectId +
            ", projectName='" + projectName + '\'' +
            ", vendor=" + vendor +
            ", client='" + client + '\'' +
            ", billRate=" + billRate +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", status='" + status + '\'' +
            ", invoiceTerm='" + invoiceTerm + '\'' +
            ", paymentTerm='" + paymentTerm + '\'' +
            ", projectNotes=" + projectNotes +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}