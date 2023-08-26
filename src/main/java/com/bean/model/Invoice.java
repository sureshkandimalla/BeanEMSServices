package com.bean.model;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Invoice")
public class Invoice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long invoiceId;

  private LocalDate startDate;
  private LocalDate endDate;
  private double hours;
  private double total;
  private LocalDate invoiceDate;
  private String status;
  private LocalDate paymentDate;

  @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  //@JsonIgnore
  private Assignment assignment;

  public Invoice(LocalDate startDate, LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  @Override
  public String toString() {
    return "{" +
      " id='" + getInvoiceId() + "'" +
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
