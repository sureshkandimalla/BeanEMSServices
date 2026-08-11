package com.employeehub.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

// One employee's hours for a single pay period, cross-referencing three
// independently-recorded sources (see HoursReportController):
//  - payrollHours:   what Payroll says they were paid for (imported/entered
//                     per pay period, not derived from timesheets)
//  - timesheetHours: what TimeSheet says they actually logged (summed from
//                     daily entries falling inside the pay period)
//  - invoicedHours:  what Bills says was billed to the client, prorated by
//                     day-overlap since a Bills row's own billing window
//                     rarely lines up exactly with a pay period
@Setter
@Getter
public class EmployeeHoursReportRow {
    private Long employeeId;
    private String employeeName;
    private Long payPeriodId;
    private LocalDate payPeriodStartDate;
    private LocalDate payPeriodEndDate;
    private float payrollHours;
    private float timesheetHours;
    private float invoicedHours;
    // True invoiced hours are prorated (see class comment) — flagged so the
    // UI can visually distinguish an estimate from an exact figure.
    private boolean invoicedHoursEstimated;
    private boolean hasDiscrepancy;
    private String discrepancyDetail;
}
