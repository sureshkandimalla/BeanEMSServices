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
@Table(name = "BankAccount")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BankAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bankAccountID;

  private String bankName;
  @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) @MapsId
  private Address bankAddress;
  @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) @MapsId
  private Address accountAddress;
  private String accountNumber;
  private String routingNumber;
  private String accountType;
  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JoinColumn(name = "bankAccountID")
  private List<Notes> bankAccountNotes;
  private LocalDate activeFrom;
  private LocalDate activeUntil;
  @UpdateTimestamp
  private LocalDate LastUpdated;
}
