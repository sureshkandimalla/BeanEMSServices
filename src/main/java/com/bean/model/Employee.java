package com.bean.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  @Column(name = "employee_id")
  private long employeeId;
  private String firstName;
  private String lastName;
  private String emailId;
  private String phone;
  private String dob;
  private enum gender {MALE,FEMALE};
  private String ssn;
  private String visa;
  private String taxTerm;
  private String referredBy;
  private String gender;
  private LocalDate startDate;
  private LocalDate endDate;
  private String designation;
  private String employmentType;
  private String status;
  private String location;
  private String primarySkills;
  private String secondarySkills;
	/*
	 * @OneToMany(cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "employeeId") private List<Assignment>
	 * employeeAssignments;
	 */
  
  @JsonIgnoreProperties("employee")
  @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Address address;
  
  @JsonIgnoreProperties("employee")
  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
  private List<Project> project = new ArrayList<>();

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
	return "Employee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId="
			+ emailId + ", phone=" + phone + ", dob=" + dob + ", ssn=" + ssn + ", visa=" + visa + ", taxTerm=" + taxTerm
			+ ", referredBy=" + referredBy + ", gender=" + gender + ", startDate=" + startDate + ", endDate=" + endDate
			+ ", designation=" + designation + ", employmentType=" + employmentType + ", LastUpdated=" + LastUpdated + "]";
}
  
  
}
