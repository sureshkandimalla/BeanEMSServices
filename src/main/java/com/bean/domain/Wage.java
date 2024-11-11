package com.bean.domain;

import com.bean.model.Notes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class Wage {


  private Long projectId;
  private long wageId;
  private float wage;
  private String wageType;
  private LocalDate startDate;
  private LocalDate EndDate;
  private Notes wageNotes;
  private LocalDate createdDate;

}
