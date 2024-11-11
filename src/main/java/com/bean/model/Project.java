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
  @Column(name = "project_id")
  private Long projectId;

  private String projectName;
  
  @JsonIgnoreProperties("project")
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "vendor_id", referencedColumnName = "customer_id")
  private Customer customer;
  @JsonIgnoreProperties("project")
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employee_id")
  private Employee employee;
  private String client;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "projectId")
  private List<Wage> billRates;
  private LocalDate startDate;
  private LocalDate endDate;
  private String invoiceTerm;
  private String paymentTerm;

  private String status;
  /*@OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "projectId")
    private List<Notes> projectNotes;*/

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "projectId")
  private List<Assignment> assignments;

  @UpdateTimestamp
  private LocalDate LastUpdated;
  
  //using below to access the UI request only
  @Transient
  private Long employeeId;
  @Transient
  private Long vendorId;
  
  @Override
public String toString() {
	return "Project [projectId=" + projectId + ", projectName=" + projectName + ", client=" + client + ", startDate="
			+ startDate + ", endDate=" + endDate + ", invoiceTerm=" + invoiceTerm + ", paymentTerm=" + paymentTerm
			+ ", status=" + status + ", LastUpdated=" + LastUpdated +", assignments= "+assignments+ "]";
}

}