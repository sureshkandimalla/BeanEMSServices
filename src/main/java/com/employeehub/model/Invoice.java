package com.employeehub.model;

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
  private Long id;

  // Cosmetic/business label only — NOT unique, NOT used for identity or
  // lookups. Historically this was the @Id itself (named invoiceId), which
  // let two different user-typed numbers collide and silently overwrite
  // each other's invoice row; `id` above is the real identity now.
  private Long invoiceNumber;

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
  private LocalDate invoicePaidDate;
  private float discounts;
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
              "id=" + id +
              ", invoiceNumber=" + invoiceNumber +
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
              ", invoicePaidDate=" + invoicePaidDate +
              ", discounts=" + discounts +
              ", status='" + status + '\'' +
              '}';
  }

}
