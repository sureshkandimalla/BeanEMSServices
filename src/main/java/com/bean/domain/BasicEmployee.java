package com.bean.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;

@Setter
@Getter

public class BasicEmployee {
  private long employeeId;
  private String name;

  private String status;


    public BasicEmployee(BigInteger employeeId, String name, String status) {
        this.employeeId = employeeId.longValue();
        this.name = name;
        this.status = status;
    }
    public BasicEmployee(Integer employeeId, String name, String status) {
        this.employeeId = employeeId.longValue();
        this.name = name;
        this.status = status;
    }




}
