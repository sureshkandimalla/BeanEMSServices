package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "Address")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long addressId;
  private String address;
  private String city;
  private String state;
  private String zipCode;
  private String country;

  /*@OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "addressId")
  private List<EmployeeAddress> employeeAddress;*/
	/*
	 * @OneToMany(cascade = CascadeType.ALL)
	 * 
	 * @JoinColumn(name = "addressId") private List<Notes> addressNotes;
	 */
  
  @JsonIgnoreProperties("address")
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", referencedColumnName = "employee_id")
  private Employee employee;

  @UpdateTimestamp
  private LocalDate LastUpdated;

@Override
public String toString() {
	return "Address [addressId=" + addressId + ", address=" + address + ", city=" + city + ", state=" + state
			+ ", zipCode=" + zipCode + ", country=" + country + ", LastUpdated="
			+ LastUpdated + "]";
}
  
  
}
