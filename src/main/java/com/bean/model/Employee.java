package com.bean.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
  private String referredBy;
  private String gender;
  private LocalDate startDate;
  private LocalDate endDate;
  private String designation;
  private String employmentType;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "employeeId")
  private List<Assignment> employeeAssignments;
  @OneToMany(mappedBy = "employee" , cascade = CascadeType.ALL)
 // @JoinColumn(name = "employeeId")
  private List<EmployeeAddress> employeeAddress;


 /* @OneToMany(mappedBy = "employee" , cascade = CascadeType.ALL)
 // @JoinColumn(name = "employeeId")
  private List<ImmigrationDetails> employeeImmigrationDetails;
*/

	/*@OneToMany(cascade = CascadeType.ALL)
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
  private List<EmployeeAssignments> employeeAssignments;*/

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
           // ", employeeImmigrationDetails=" + employeeImmigrationDetails +
            ", referredBy='" + referredBy + '\'' +
           /* ", employmentDetails=" + employmentDetails +
            ", employeeBankAccount=" + employeeBankAccount +
            ", employeeNotes=" + employeeNotes +*/
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
