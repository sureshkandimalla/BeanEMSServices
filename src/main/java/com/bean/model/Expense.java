package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "expenses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Expense {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "expense_id")
  private Long expenseId;

  @Column(name = "employee_id")
  private Long employeeId;

  private String description;

  @Column(name = "expense_type")
  private String expenseType;

  private double amount;

  private boolean reimbursable;

  @Column(name = "expense_date")
  private LocalDate expenseDate;

  private String status;

}
