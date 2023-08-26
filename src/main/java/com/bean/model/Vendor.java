package com.bean.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@Entity
@Table(name = "Vendor")
public class Vendor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long vendorId;

  private String vendorCompanyName;
  private String vendorName;
  private String vendorPhone;
  private String vendorEmail;
  private String ein;
  @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
  @JoinColumn(name = "vendorId")
  private Address vendorAddress;
  private String website;
  private String vendorStatus;
  private LocalDate vendorStartDate;
  private LocalDate vendorEndDate;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "vendorId")
  private List<BankAccount> vendorBankAccount;


 /* @OneToMany(mappedBy = "vendorId")
  private List<Project> projects = new ArrayList<>();*/

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "Vendor{" +
            "vendorId=" + vendorId +
            ", vendorCompanyName='" + vendorCompanyName + '\'' +
            ", vendorName='" + vendorName + '\'' +
            ", vendorPhone='" + vendorPhone + '\'' +
            ", vendorEmail='" + vendorEmail + '\'' +
            ", ein='" + ein + '\'' +
            ", vendorAddress=" + vendorAddress +
            ", website='" + website + '\'' +
            ", vendorStatus='" + vendorStatus + '\'' +
            ", vendorStartDate=" + vendorStartDate +
            ", vendorEndDate=" + vendorEndDate +
            ", vendorBankAccount=" + vendorBankAccount +
    //        ", projects=" + projects +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
