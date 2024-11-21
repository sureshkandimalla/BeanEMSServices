package com.bean.domain;

import com.bean.model.Notes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter

public class Assignment {

  private String firstName;
  private String lastName;
  private BigInteger assignmentId;
  private Double wage;
  private Date startDate;
  private Date endDate;

  private String assignmentType;

  private String status;


  private Date lastUpdatedDate;

  public Assignment(String firstName, String lastName,
                    BigInteger assignmentId, double wage,
                    String assignmentType, String status,Date startDate, Date endDate,  Date lastUpdatedDate) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.assignmentId = assignmentId;
    this.wage = wage;
    this.startDate = startDate;
    this.endDate = endDate;
    this.assignmentType = assignmentType;
    this.status = status;
    this.lastUpdatedDate = lastUpdatedDate;
  }
}
