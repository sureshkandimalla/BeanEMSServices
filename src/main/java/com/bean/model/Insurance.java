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
@Table(name = "Insurance")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Insurance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  private long employeeId;
  private double amount;
  private String InsuranceType;
  private String notes;
  private LocalDate month;
  @UpdateTimestamp
  private LocalDate LastUpdated;


}
