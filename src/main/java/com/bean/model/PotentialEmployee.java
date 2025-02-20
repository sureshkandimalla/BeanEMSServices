package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "PEmployees")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PotentialEmployee {

  @Id
 // @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "peid")
  private long peId;
  private String firstName;
  private String lastName;
  private String emailId;
  private String phone;
  private String dob;
  private String company;
  private String visaType;
  private enum gender {MALE,FEMALE};
  private String referredBy;
  private String gender;
  private String year;
  private LocalDate startDate;
  private LocalDate endDate;
  private String status;
  private String primarySkills;
  private String secondarySkills;
  private String currentLocation;
  private String workCountry;
  private String onProject;
  @UpdateTimestamp
  private LocalDate LastUpdated;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "peId")
  private List<Notes> peNotes;
}
