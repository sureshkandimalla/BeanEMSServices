package com.bean.model;

import java.time.LocalDate;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

// A Vendor supplies resources or a service to us (e.g. the staffing agency
// that places a C2C employee) — the inverse relationship of a Customer,
// who we supply a service to. Structurally mirrors Customer.
@Setter
@Getter
@Entity
@Table(name = "vendor")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vendor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "vendor_id")
  private Long vendorId;

  private String vendorCompanyName;
  private String vendorName;
  private String vendorPhone;
  private String vendorEmail;
  private String ein;
  private String website;
  private String vendorStatus;
  private LocalDate vendorStartDate;
  private LocalDate vendorEndDate;
  private String vendorContactEmail;
  private String vendorAddress;

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "vendor{" +
            "vendorId=" + vendorId +
            ", vendorCompanyName='" + vendorCompanyName + '\'' +
            ", vendorName='" + vendorName + '\'' +
            ", vendorPhone='" + vendorPhone + '\'' +
            ", vendorEmail='" + vendorEmail + '\'' +
            ", ein='" + ein + '\'' +
            ", website='" + website + '\'' +
            ", vendorStatus='" + vendorStatus + '\'' +
            ", vendorStartDate=" + vendorStartDate +
            ", vendorEndDate=" + vendorEndDate +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
