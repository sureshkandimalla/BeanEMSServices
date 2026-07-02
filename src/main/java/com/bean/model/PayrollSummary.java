package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "payroll_summary")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PayrollSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_summary_id")
    private Long payrollSummaryId;

    private Long payPeriodId;
    private Long employeeId;

    private String payFrequency;
    private String department;
    private LocalDate checkDate;
    private String employeeName;
    private Float hours;
    private Float totalPaid;
    private Float taxWithheld;
    private Float deductions;
    private Float netPay;
    private String paymentDetails;
    private Float employerLiability;
    private Float totalExpenses;

    @UpdateTimestamp
    private LocalDate lastUpdated;

    @Transient
    private LocalDate payPeriodStartDate;

    @Transient
    private LocalDate payPeriodEndDate;
}
