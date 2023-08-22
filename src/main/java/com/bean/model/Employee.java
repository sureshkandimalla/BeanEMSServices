package com.bean.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

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
  private String employeeType;
  private String visa;
  private String dob;
  private String vendor;
  private String phone;
  private String address;
  private String city;
  private String state;
  private String referredBy;

  

/* @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employeeAssignment_employeeId", referencedColumnName = "employeeId")
  private List<Assignment> assignments = new ArrayList<>();

     @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id")
		private Project project;*/
	

	@Override
	public String toString() {
		return "{" +
			" id='" + getEmployeeId() + "'" +
			", firstName='" + getFirstName() + "'" +
			", lastName='" + getLastName() + "'" +
			", emailId='" + getEmailId() + "'" +
			", employeeType='" + getEmployeeType() + "'" +
			", visa='" + getVisa() + "'" +
			", dob='" + getDob() + "'" +
			", vendor='" + getVendor() + "'" +
			", phone='" + getPhone() + "'" +
			", address='" + getAddress() + "'" +
			", city='" + getCity() + "'" +
			", state='" + getState() + "'" +
			", referredBy='" + getReferredBy() + "'" +
		//	", assignments='" + getAssignments() + "'" +
		//	", project='" + getProject() + "'" +
			"}";
	}


}
