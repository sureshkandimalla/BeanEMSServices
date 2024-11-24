package com.bean.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "PayPeriods")
public class PayPeriod {

  @Id
  //@GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long payPeriodId;
  private LocalDate payDate;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate paymentDate;
  private String status;
  

}
