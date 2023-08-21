package net.javaguides.springboot.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Invoice")
public class Invoice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String vendorName;
  private double billRate;
  private LocalDate startDate;
  private LocalDate endDate;
  private double hours;
  private double total;
  private LocalDate invoiceDate;
  private String status;
  private LocalDate paymentDate;

  

  @Override
  public String toString() {
    return "{" +
      " id='" + getId() + "'" +
      ", vendorName='" + getVendorName() + "'" +
      ", billRate='" + getBillRate() + "'" +
      ", startDate='" + getStartDate() + "'" +
      ", endDate='" + getEndDate() + "'" +
      ", hours='" + getHours() + "'" +
      ", total='" + getTotal() + "'" +
      ", invoiceDate='" + getInvoiceDate() + "'" +
      ", status='" + getStatus() + "'" +
      ", paymentDate='" + getPaymentDate() + "'" +
      "}";
  }

}
