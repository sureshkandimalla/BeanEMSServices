package com.employeehub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

// Certificate of Insurance — a vendor/customer's insurance coverage record
// (not to be confused with model.Insurance, an unrelated per-employee
// payroll deduction, or model.HealthInsurance, an unrelated CSV-imported
// premium/claim record).
@Setter
@Getter
@Entity
@Table(name = "Coi")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Coi {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate startDate;
  private LocalDate endDate;
  private Long vendorId;
  private Long customerId;
  private String status;

  @Column(columnDefinition = "TEXT")
  private String limits;

  @UpdateTimestamp
  private LocalDate lastUpdated;
}
