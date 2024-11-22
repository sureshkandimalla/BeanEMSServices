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
@Table(name = "adjustments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Adjustment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "adjustment_id")
  private long adjustmentId;
  private long fromId;
  private long toId;
  /*@ManyToOne
  @JoinColumn(name = "from_id", referencedColumnName = "employee_id")
  private Employee fromEmployee; // Maps to 'from_id'

  @ManyToOne
  @JoinColumn(name = "to_id", referencedColumnName = "employee_id")
  private Employee toEmployee;*/
  private double amount;
  private String adjustmentType;
  private String notes;
  private LocalDate adjustmentDate;
  @UpdateTimestamp
  private LocalDate LastUpdated;


}
