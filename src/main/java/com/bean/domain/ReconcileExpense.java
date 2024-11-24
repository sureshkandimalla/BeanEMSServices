package com.bean.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ReconcileExpense {
    private String description;
    private float hours;
    private String expenseType;
    private float wage;
    private float total;
    private long employeeId;
    private long projectId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;


    public String toString() {
        return "ReconcileExpense{" +
                "description: " + description +
                ", hours=" + hours +
                ", wage=" + wage +
                ", income=" + total +
                '}';
    }
}
