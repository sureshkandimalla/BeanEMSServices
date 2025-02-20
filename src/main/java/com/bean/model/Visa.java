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
@Table(name = "Visa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Visa {

  @Id
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
  private Long lcaWage;
  private String status;
  @JsonIgnoreProperties("visa")
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
  private Employee employee;

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
                ", employee=" + employee +
                ", LastUpdated=" + LastUpdated +
                '}';
    }
}
