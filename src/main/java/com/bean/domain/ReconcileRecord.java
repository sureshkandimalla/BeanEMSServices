package com.bean.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class ReconcileRecord {
    private String description;
    private float hours;
    private float projectBilling;
    private float wage;
    private float income;
    private float expense;
    private float invoiceTotal;
    private float invoicePaidAmount;
    private List<ReconcileExpense> expenseRecords;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private long projectId;



    public String toString() {
        return "ReconcileRecord{" +
                "description: " + description +
                ", hours=" + hours +
                ", wage=" + wage +
                ", income=" + income +
                ", expense=" + expense +
                '}';
    }
}
