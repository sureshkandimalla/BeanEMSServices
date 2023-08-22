package com.bean.model;

import java.time.LocalDate;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

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
  private String vendor;
  private String client;
  private double billRate;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;
  private String invoiceTerm;
  private String paymentTerm;
  private String notes;
  /*@ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "id")
  private Employee Employee;*/

  @Override
  public String toString() {
    return "{" +
      " id='" + getProjectId() + "'" +
      ", projectName='" + getProjectName() + "'" +
      ", vendor='" + getVendor() + "'" +
      ", client='" + getClient() + "'" +
      ", billRate='" + getBillRate() + "'" +
      ", startDate='" + getStartDate() + "'" +
      ", endDate='" + getEndDate() + "'" +
      ", status='" + getStatus() + "'" +
      ", invoiceTerm='" + getInvoiceTerm() + "'" +
      ", paymentTerm='" + getPaymentTerm() + "'" +
      ", notes='" + getNotes() + "'" +
      "}";
  }

   
}