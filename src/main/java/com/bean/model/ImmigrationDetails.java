package com.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "ImmigrationType")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ImmigrationDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ImmigrationDetailsId;
  private String visaType;
  private LocalDate visaStartDate;
  private LocalDate visaEndDate;
  @UpdateTimestamp
  private LocalDate lastUpdated;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ImmigrationDetailsId")
  private List<Notes> immigrationNotes;

  @Override
  public String toString() {
    return "ImmigrationDetails{" +
            "ImmigrationDetailsId=" + ImmigrationDetailsId +
            ", visaType='" + visaType + '\'' +
            ", visaStartDate=" + visaStartDate +
            ", visaEndDate=" + visaEndDate +
            ", lastUpdated=" + lastUpdated +
            ", immigrationNotes=" + immigrationNotes +
            '}';
  }
}
