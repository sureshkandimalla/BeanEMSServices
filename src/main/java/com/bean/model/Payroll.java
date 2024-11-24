package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "payroll")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payroll {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payroll_id")
  private long payrollId;
  private long payCheckId;
  private long employeeId;
  @Transient
  private LocalDate checkDate;
  @Transient
  private LocalDate payCycleStartDate;
  @Transient
  private LocalDate payCycleEndDate;
  private float hours;
  private float totalPaid;
  private float taxWithheld;
  private float deductions;
  private float netPay;
  private float employerLiability;
  private String notes;
  @UpdateTimestamp
  private LocalDate LastUpdated;
public Payroll() {}
  public Payroll(long payrollId, long payCheckId, long employeeId, LocalDate payCycleStartDate,LocalDate checkDate,  LocalDate payCycleEndDate, float hours, float totalPaid, float taxWithheld, float deductions, float netPay, float employerLiability, String notes, LocalDate lastUpdated) {
    this.payrollId = payrollId;
    this.payCheckId = payCheckId;
    this.employeeId = employeeId;
    this.checkDate = checkDate;
    this.payCycleStartDate = payCycleStartDate;
    this.payCycleEndDate = payCycleEndDate;
    this.hours = hours;
    this.totalPaid = totalPaid;
    this.taxWithheld = taxWithheld;
    this.deductions = deductions;
    this.netPay = netPay;
    this.employerLiability = employerLiability;
    this.notes = notes;
    LastUpdated = lastUpdated;
  }

  @Override
  public String toString() {
    return "Payroll{" +
            "payrollId=" + payrollId +
            ", employeeId=" + employeeId +
            ", checkDate=" + checkDate +
            ", payCycleStartDate=" + payCycleStartDate +
            ", payCycleEndDate=" + payCycleEndDate +
            ", hours=" + hours +
            ", totalPaid=" + totalPaid +
            ", taxWithheld=" + taxWithheld +
            ", deductions=" + deductions +
            ", netPay=" + netPay +
            ", employerLiability=" + employerLiability +
            ", notes='" + notes + '\'' +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
