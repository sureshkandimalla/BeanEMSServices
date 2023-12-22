package com.bean.model;
import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EmployeeAddressPK implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    private Long employeeId;
 
    private Long AddressId;
 
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(employeeId, AddressId);
    }
 
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EmployeeAddressPK other = (EmployeeAddressPK) obj;
        return Objects.equals(AddressId, other.AddressId) && Objects.equals(employeeId, other.employeeId);
    }
 
}