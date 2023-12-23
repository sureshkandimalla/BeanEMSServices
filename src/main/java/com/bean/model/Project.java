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

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "vendorId")
  private Customer customer;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
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

}