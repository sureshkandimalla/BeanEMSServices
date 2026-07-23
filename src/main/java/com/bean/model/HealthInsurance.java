package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "health_insurance")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HealthInsurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_insurance_id")
    private Long healthInsuranceId;

    private String groupId;
    private LocalDate dateOfBill;
    private LocalDate dueDate;
    private String employeeName;
    private Long employeeId;
    private String umi;
    private Float claimPrefund;
    private Float specificStopLoss;
    private Float aggregateStopLoss;
    private Float adminFee;
    private Float total;
    private String comment;

    @UpdateTimestamp
    private LocalDate lastUpdated;
}
