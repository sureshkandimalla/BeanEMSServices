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
@Table(name = "passport")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passportId;

    private String passportNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String nationality;
    private String countryOfBirth;
    private LocalDate dateOfBirth;
    private String gender;
    private String placeOfIssue;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String status;   // e.g. Active, Expired, Renewed

    @JsonAlias("employeeId")
    @JsonDeserialize(using = EmployeeDeserializer.class)
    @JsonIgnoreProperties({"visas", "address", "project", "passports"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
    private Employee employee;

    @UpdateTimestamp
    private LocalDate lastUpdated;
}
