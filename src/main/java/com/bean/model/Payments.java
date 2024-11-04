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
@Table(name = "payments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payments {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private long paymentId;
  private long employeeId;
  private double amount;
  private int paymentType;
  private String notes;
  @UpdateTimestamp
  private LocalDate LastUpdated;

    @Override
    public String toString() {
        return "Payments{" +
                "paymentId=" + paymentId +
                ", employeeId=" + employeeId +
                ", paymentType=" + paymentType +
                ", notes='" + notes + '\'' +
                ", LastUpdated=" + LastUpdated +
                '}';
    }
}
