package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "ProjectBilling")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProjectBilling {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long projectBillingId;
  @OneToOne @MapsId
  private Wage projectBilling;
  @UpdateTimestamp
  private LocalDate LastUpdated;
}
