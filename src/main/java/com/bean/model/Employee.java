package com.bean.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@Entity
@Table(name = "employees")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long employeeId;
  private String firstName;
  private String lastName;
  private String emailId;
  private String phone;
  private String dob;
  private enum gender {MALE,FEMALE};
  private String ssn;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private List<Address> employeeAddress;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private List<ImmigrationDetails> employeeImmigrationDetails;

  private String referredBy;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employeeId")
	private List<Employment> employmentDetails;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private List<BankAccount> employeeBankAccount;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private List<Notes> employeeNotes;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private List<Project> projects;

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "Employee{" +
            "employeeId=" + employeeId +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", emailId='" + emailId + '\'' +
            ", phone='" + phone + '\'' +
            ", dob='" + dob + '\'' +
            ", ssn='" + ssn + '\'' +
            ", employeeAddress=" + employeeAddress +
            ", employeeImmigrationDetails=" + employeeImmigrationDetails +
            ", referredBy='" + referredBy + '\'' +
            ", employmentDetails=" + employmentDetails +
            ", employeeBankAccount=" + employeeBankAccount +
            ", employeeNotes=" + employeeNotes +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
