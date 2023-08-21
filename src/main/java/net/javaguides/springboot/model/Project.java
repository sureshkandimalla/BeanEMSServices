package net.javaguides.springboot.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Project")
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;



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


  @Override
  public String toString() {
    return "{" +
      " id='" + getId() + "'" +
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