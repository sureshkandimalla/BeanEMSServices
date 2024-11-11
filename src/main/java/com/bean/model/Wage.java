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
@Table(name = "Wage")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Wage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long wageId;
  private float wage;
  private String wageType;
  private LocalDate startDate;
  private LocalDate EndDate;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "wageId")
  private List<Notes> wageNotes;


  private LocalDate createdDate;

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "Wage{" +
            "wageId=" + wageId +
            ", wage=" + wage +
            ", startDate=" + startDate +
            ", EndDate=" + EndDate +
            ", wageNotes=" + wageNotes +
            ", createdDate=" + createdDate +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
