package com.bean.service;

import com.bean.exception.ResourceNotFoundException;
import com.bean.model.Employee;
import com.bean.model.LCA;
import com.bean.model.Visa;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.LCARepository;
import com.bean.repository.VisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisaService {

    @Autowired
    private VisaRepository visaRepository;

    @Autowired
    private LCARepository lcaRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Visa> getAllVisas() {
        return visaRepository.findAll();
    }

    public Optional<Visa> getVisaById(Long id) {
        return visaRepository.findByVisaId(id);
    }

    public List<Visa> getVisasByEmployeeId(Long employeeId) {
        return visaRepository.findByEmployee_EmployeeId(employeeId);
    }

    public Optional<Visa> getLatestVisaByEmployeeId(Long employeeId) {
        return visaRepository.findFirstByEmployee_EmployeeIdOrderByStartDateDesc(employeeId);
    }

    public Visa saveVisa(Visa visa) {
        if (visa.getVisaId() != 0) {
            return updateVisa(visa.getVisaId(), visa);
        }
        resolveRelations(visa, visa);
        return visaRepository.save(visa);
    }

    public Visa updateVisa(Long id, Visa visaDetails) {
        Visa visa = visaRepository.findByVisaId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visa not found with id: " + id));

        visa.setVisaCategory(visaDetails.getVisaCategory());
        visa.setReceiptNumber(visaDetails.getReceiptNumber());
        visa.setStartDate(visaDetails.getStartDate());
        visa.setEndDate(visaDetails.getEndDate());
        visa.setJobTitle(visaDetails.getJobTitle());
        visa.setLcaNumber(visaDetails.getLcaNumber());
        visa.setSocCode(visaDetails.getSocCode());
        visa.setClient(visaDetails.getClient());
        visa.setVendor(visaDetails.getVendor());
        visa.setJobLocation(visaDetails.getJobLocation());
        visa.setJobLocation2(visaDetails.getJobLocation2());
        visa.setLcaWage(visaDetails.getLcaWage());
        visa.setStatus(visaDetails.getStatus());
        visa.setFilingType(visaDetails.getFilingType());
        visa.setVisaSubCategory(visaDetails.getVisaSubCategory());
        visa.setFilingYear(visaDetails.getFilingYear());

        resolveRelations(visa, visaDetails);

        return visaRepository.save(visa);
    }

    /** Resolves employee and lca from their IDs and sets them on target */
    private void resolveRelations(Visa target, Visa source) {
        // Resolve Employee
        if (source.getEmployee() != null && source.getEmployee().getEmployeeId() != 0) {
            Employee emp = employeeRepository.findById(source.getEmployee().getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + source.getEmployee().getEmployeeId()));
            target.setEmployee(emp);
        }
        // Resolve LCA
        if (source.getLca() != null) {
            if (source.getLca().getLcaId() != 0) {
                LCA lca = lcaRepository.findById(source.getLca().getLcaId())
                        .orElseThrow(() -> new ResourceNotFoundException("LCA not found with id: " + source.getLca().getLcaId()));
                target.setLca(lca);
            } else {
                // No LCA id provided — save provided LCA (includes new shortName) and attach
                LCA saved = lcaRepository.save(source.getLca());
                target.setLca(saved);
            }
        } else {
            target.setLca(null);
        }
    }

    public void deleteVisa(Long id) {
        if (!visaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Visa not found with id: " + id);
        }
        visaRepository.deleteById(id);
    }
}
