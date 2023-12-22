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

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "addressId")
  private List<EmployeeAddress> employeeAddress;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "addressId")
  private List<Notes> addressNotes;

  @Override
  public String toString() {
    return "Address{" +
            "addressId=" + addressId +
            ", address='" + address + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", zipCode='" + zipCode + '\'' +
            ", country='" + country + '\'' +
            ", addressNotes=" + addressNotes +
            '}';
  }
  @UpdateTimestamp
  private LocalDate LastUpdated;
}
