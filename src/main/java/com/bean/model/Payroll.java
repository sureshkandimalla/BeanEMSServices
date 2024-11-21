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
  private long employeeId;
  private LocalDate checkDate;
  private LocalDate payCycleStartDate;
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
