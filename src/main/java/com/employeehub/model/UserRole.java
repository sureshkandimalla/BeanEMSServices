package com.employeehub.model;

import java.time.LocalDate;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

// Maps a user's email to exactly one role (ADMIN | IMMIGRATION | HR |
// ACCOUNTING) for frontend page gating (see BeanEMS's src/Utils/roleAccess.js).
// A user with no row here is treated as "no access assigned yet" — an Admin
// grants access by inserting/updating a row through UserRoleController.
@Setter
@Getter
@Entity
@Table(name = "user_role")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String role;

  @UpdateTimestamp
  private LocalDate lastUpdated;
}
