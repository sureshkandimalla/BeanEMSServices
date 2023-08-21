package net.javaguides.springboot.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "Vendor")
public class Vendor {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String vendorName;

  @OneToMany(mappedBy = "vendor")
  private List<Project> projects = new ArrayList<>();


  @Override
  public String toString() {
    return "{" +
      " id='" + getId() + "'" +
      ", vendorName='" + getVendorName() + "'" +
      ", projects='" + getProjects() + "'" +
      "}";
  }
  

}
