package com.bean.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;

@Setter
@Getter

public class Adjustment {
  private long adjustmentId;
  private long fromId;
  private long toId;
  private String fromName;
  private String toName;
  private double amount;
  private String adjustmentType;
  private String notes;
  private LocalDate adjustmentDate;
  private LocalDate lastUpdated;

  public Adjustment(){
  }
  public Adjustment(Object row[]){
    this.adjustmentId = row[0] != null ? ((BigInteger) row[0]).longValue() : null;
    this.lastUpdated = row[1] != null ? (((Date) row[1])).toLocalDate() : null;
    this.adjustmentDate = row[2] != null ? (((Date) row[2])).toLocalDate() : null;
    this.adjustmentType = (String)row[3];

    this.amount = row[4] != null ? ((Double) row[4]) : null;
    this.notes = (String)row[5];
    this.fromId=row[6] != null ? ((BigInteger) row[6]).intValue() : null;
    this.toId=row[7] != null ? ((BigInteger) row[7]).intValue() : null;
    this.fromName = (String)row[8];
    this.toName = (String)row[9];

  }



}
