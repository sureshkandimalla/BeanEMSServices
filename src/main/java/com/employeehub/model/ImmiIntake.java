package com.employeehub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

// One row per immigration case intake for an employee (an employee can have
// several over time — new H1B, transfer, extension, etc.) — the working
// list immigration/HR uses to track a case end-to-end from filing through
// LCA/receipt details, independent of the Visa/LCA records those cases may
// eventually produce.
@Setter
@Getter
@Entity
@Table(name = "immi_intake")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ImmiIntake {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "intake_id")
  private long intakeId;

  private Long employeeId;
  private String employeeName;
  private String caseId;
  private String visaStatus;
  private String visaSub;
  private String filingType;
  private String filingYear;
  private String applicationStatus;
  private String caseAssignedTo;
  private String filedOn;
  private String premiumStatus;
  private String receiptNumber;
  private LocalDate startDate;
  private LocalDate endDate;
  private String lcaTitle;
  private String lcaCaseNumber;
  private Double lcaWages;
  private String client;
  private String vendor;
  private String workLocation1;
  private String workLocation2;
  private LocalDate caseFiledDate;
  private Integer caseRank;

  @UpdateTimestamp
  private LocalDate lastUpdated;
}
