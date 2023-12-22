package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "EmployeeAddress")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeAddress implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long employeeAddressId;


  @EmbeddedId
  private EmployeeAddressPK id;
 // private List<Address> AddressList;

  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("employeeId")
  @JsonIgnore
  private Employee employee;
  @ManyToOne(fetch = FetchType.EAGER)
  @MapsId("addressId")
 // @JsonIgnore
  private Address address;
  @UpdateTimestamp
  private LocalDate LastUpdated;
}
