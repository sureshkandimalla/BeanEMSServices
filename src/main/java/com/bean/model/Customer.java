package com.bean.model;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@Entity
@Table(name = "customer")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customer_id")
  private Long customerId;

  private String customerCompanyName;
  private String customerName;
  private String customerPhone;
  private String customerEmail;
  private String ein;
  /*@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JoinColumn(name = "customerId")
  private Address customerAddress;*/
  private String website;
  private String customerStatus;
  private LocalDate customerStartDate;
  private LocalDate customerEndDate;

 /* @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "customerId")
  private List<BankAccount> customerBankAccount;


  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "customerId")
  private List<Project> projects;*/

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "customer{" +
            "customerId=" + customerId +
            ", customerCompanyName='" + customerCompanyName + '\'' +
            ", customerName='" + customerName + '\'' +
            ", customerPhone='" + customerPhone + '\'' +
            ", customerEmail='" + customerEmail + '\'' +
            ", ein='" + ein + '\'' +
           // ", customerAddress=" + customerAddress +
            ", website='" + website + '\'' +
            ", customerStatus='" + customerStatus + '\'' +
            ", customerStartDate=" + customerStartDate +
            ", customerEndDate=" + customerEndDate +
           /// ", customerBankAccount=" + customerBankAccount +
    //        ", projects=" + projects +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
