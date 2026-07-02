package com.bean.service;

import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Employee;
import com.bean.model.Passport;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassportService {

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Passport> getAllPassports() {
        return passportRepository.findAll();
    }

    public Optional<Passport> getPassportById(Long id) {
        return passportRepository.findById(id);
    }

    public List<Passport> getPassportsByEmployeeId(Long employeeId) {
        return passportRepository.findByEmployee_EmployeeId(employeeId);
    }

    public Passport savePassport(Passport passport) {
        resolveEmployee(passport);
        return passportRepository.save(passport);
    }

    public Passport updatePassport(Long id, Passport details) {
        Passport passport = passportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Passport not found with id: " + id));

        passport.setPassportNumber(details.getPassportNumber());
        passport.setFirstName(details.getFirstName());
        passport.setLastName(details.getLastName());
        passport.setMiddleName(details.getMiddleName());
        passport.setNationality(details.getNationality());
        passport.setCountryOfBirth(details.getCountryOfBirth());
        passport.setDateOfBirth(details.getDateOfBirth());
        passport.setGender(details.getGender());
        passport.setPlaceOfIssue(details.getPlaceOfIssue());
        passport.setIssueDate(details.getIssueDate());
        passport.setExpiryDate(details.getExpiryDate());
        passport.setStatus(details.getStatus());

        if (details.getEmployee() != null) {
            resolveEmployee(details);
            passport.setEmployee(details.getEmployee());
        }

        return passportRepository.save(passport);
    }

    public void deletePassport(Long id) {
        if (!passportRepository.existsById(id)) {
            throw new ResourceNotFoundException("Passport not found with id: " + id);
        }
        passportRepository.deleteById(id);
    }

    private void resolveEmployee(Passport passport) {
        if (passport.getEmployee() != null && passport.getEmployee().getEmployeeId() != 0) {
            Employee emp = employeeRepository.findById(passport.getEmployee().getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee not found with id: " + passport.getEmployee().getEmployeeId()));
            passport.setEmployee(emp);
        }
    }
}
