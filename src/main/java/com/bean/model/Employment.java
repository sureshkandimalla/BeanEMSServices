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
@Table(name = "employment")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private  long employmentId;
  private String designation;
  private enum employmentType {FULL_TIME,CONTRACT,TEMPORARY,INTERN};
  private enum taxType {W2,C2C,C1099};
  private String lcaNumber;
  //private Address employmentLocation;
  private String employmentStartDate;
  private String employmentEndDate;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employmentId")
  private List<EmployeeWage> employmentWage;

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "Employment{" +
            "employmentId=" + employmentId +
            ", designation='" + designation + '\'' +
            ", lcaNumber='" + lcaNumber + '\'' +
  //          ", employmentLocation=" + employmentLocation +
            ", employmentStartDate='" + employmentStartDate + '\'' +
            ", employmentEndDate='" + employmentEndDate + '\'' +
            ", employmentWage=" + employmentWage +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
