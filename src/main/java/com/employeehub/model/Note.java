package com.employeehub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

// Generic note, reused across entity types (Employee, Visa, LCA, Invoice,
// ...) rather than one notes table per type — type + entityId is the only
// thing that varies, same pattern as Document (entityType/entityId).
// entityId matches that type's own primary key (e.g. Employee.employeeId
// for type="Employee", LCA.lcaId for type="LCA", Invoice.id for
// type="Invoice").
@Setter
@Getter
@Entity
@Table(name = "note")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Note {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noteId;

  private String type;
  private Long entityId;

  @Column(columnDefinition = "TEXT")
  private String description;

  @CreationTimestamp
  private LocalDateTime date;

  @UpdateTimestamp
  private LocalDateTime lastUpdateDateTime;
}
