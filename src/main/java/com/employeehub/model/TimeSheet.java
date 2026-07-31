package com.employeehub.model;

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
@Table(name = "timesheet", uniqueConstraints = @UniqueConstraint(columnNames = {"assignment_id", "work_date"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TimeSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timesheet_id")
    private Long timesheetId;

    private Long employeeId;

    @Column(name = "assignment_id")
    private Long assignmentId;

    private Long projectId;

    @Column(name = "work_date")
    private LocalDate workDate;

    private Float hours;

    // Draft -> Submitted -> Approved. Only Draft rows are editable; editing
    // a Submitted row via saveBulk resets it to Draft (a changed entry is no
    // longer what was submitted). Approved rows are locked until reopened.
    private String status;

    private String notes;

    @UpdateTimestamp
    private LocalDate lastUpdated;
}
