package com.bean.model;

import com.bean.deserializer.EmployeeDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "employee_onboarding")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeOnboarding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long onboardingId;

    @JsonAlias("employeeId")
    @JsonDeserialize(using = EmployeeDeserializer.class)
    @JsonIgnoreProperties({"visas", "address", "project", "passports"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    // Dates
    private LocalDate offerLetterDate;
    private LocalDate joiningDate;
    private LocalDate orientationDate;

    // Offer & Contract
    private String offerLetterStatus;
    private String contractStatus;

    // Documents
    private String i9Status;
    private String eVerifyStatus;
    private String w4Status;
    private String directDepositStatus;
    private String backgroundCheckStatus;
    private String drugTestStatus;
    private String equipmentStatus;
    private String systemAccessStatus;
    private String benefitsEnrollmentStatus;
    private String handbookAcknowledgementStatus;

    // Agreements
    private String ndaStatus;
    private String nonCompeteStatus;

    // Work Authorization
    private String workAuthorizationType;
    private LocalDate workAuthorizationExpiry;

    // Overall
    private String onboardingStatus;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @UpdateTimestamp
    private LocalDate lastUpdated;
}
