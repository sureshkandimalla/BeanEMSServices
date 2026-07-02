package com.bean.model;

import com.bean.deserializer.EmployeeDeserializer;
import com.bean.deserializer.LcaDeserializer;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "Visa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Visa {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long visaId;
  private String visaCategory;
  private String receiptNumber ;
  private LocalDate startDate;
  private LocalDate endDate;
  private String jobTitle;
  private String lcaNumber;
  private String socCode;
  private String client;
  private String vendor;
  private String jobLocation;
  private String jobLocation2;
  private Double lcaWage;
  private String status;
  @Enumerated(EnumType.STRING)
  private FilingType filingType;
  @Enumerated(EnumType.STRING)
  private VisaSubCategory visaSubCategory;
  private Integer filingYear;
  @JsonAlias("employeeId")
  @JsonDeserialize(using = EmployeeDeserializer.class)
  @JsonIgnoreProperties("visa")
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
  private Employee employee;

  @JsonIgnoreProperties({"visa", "employee"})
  @JsonDeserialize(using = LcaDeserializer.class)
  @NotFound(action = NotFoundAction.IGNORE)
  @OneToOne(fetch = FetchType.EAGER, optional = true)
  @JoinColumn(name = "lca_id", referencedColumnName = "lcaId", nullable = true)
  private LCA lca;

  @UpdateTimestamp
  private LocalDate LastUpdated;

    @Override
    public String toString() {
        return "Visa{" +
                "visaId=" + visaId +
                ", visaCategory='" + visaCategory + '\'' +
                ", receiptNumber='" + receiptNumber + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", jobTitle='" + jobTitle + '\'' +
                ", lcaNumber='" + lcaNumber + '\'' +
                ", socCode='" + socCode + '\'' +
                ", client='" + client + '\'' +
                ", vendor='" + vendor + '\'' +
                ", jobLocation='" + jobLocation + '\'' +
                ", jobLocation2='" + jobLocation2 + '\'' +
                ", lcaWage=" + lcaWage +
                ", status='" + status + '\'' +
                ", lca=" + (lca != null ? lca.getLcaId() : null) +
                ", employee=" + employee +
                ", LastUpdated=" + LastUpdated +
                '}';
    }
}
