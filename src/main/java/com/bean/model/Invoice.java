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
  private Long invoiceId;

  private Long projectId;
  private LocalDate invoiceMonth;
  private float billing;
  private float hours;
  private float total;
  private float invoicePaidAmount;
  private LocalDate invoiceDate;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate paymentDate;
  private String status;
  
  /*@ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "assignment_id", nullable = false)
  //@JsonIgnore
  private Assignment assignment;*/

  public Invoice(LocalDate startDate, LocalDate endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public Invoice() {

  }

  @Override
  public String toString() {
      return "Invoice{" +
              "invoiceId=" + invoiceId +
              ", projectId=" + projectId +
              ", invoiceMonth=" + invoiceMonth +
              ", billing=" + billing +
              ", hours=" + hours +
              ", total=" + total +
              ", invoicePaidAmount=" + invoicePaidAmount +
              ", invoiceDate=" + invoiceDate +
              ", startDate=" + startDate +
              ", endDate=" + endDate +
              ", paymentDate=" + paymentDate +
              ", status='" + status + '\'' +
              '}';
  }

}
