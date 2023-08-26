package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "Notes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long noteId;

  private String notesType;
  private String details;

  @UpdateTimestamp
  private LocalDate LastUpdated;

  @Override
  public String toString() {
    return "Notes{" +
            "noteId=" + noteId +
            ", notesType='" + notesType + '\'' +
            ", details='" + details + '\'' +
            ", LastUpdated=" + LastUpdated +
            '}';
  }
}
