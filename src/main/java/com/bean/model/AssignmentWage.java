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
@Table(name = "AssignmentWage")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AssignmentWage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long assignmentWageId;


  @UpdateTimestamp
  private LocalDate LastUpdated;
}
