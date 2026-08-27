package com.employeehub.service;

import com.employeehub.exception.ResourceNotFoundException;
import com.employeehub.model.Coi;
import com.employeehub.repository.CoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoiService {

    @Autowired
    private CoiRepository coiRepository;

    public Coi getById(Long id) {
        return coiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("COI not exist with id: " + id));
    }

    public Coi create(Coi coi) {
        return coiRepository.save(coi);
    }

    public Coi update(Long id, Coi coiDetails) {
        Coi coi = getById(id);
        coi.setStartDate(coiDetails.getStartDate());
        coi.setEndDate(coiDetails.getEndDate());
        coi.setVendorId(coiDetails.getVendorId());
        coi.setCustomerId(coiDetails.getCustomerId());
        coi.setStatus(coiDetails.getStatus());
        coi.setLimits(coiDetails.getLimits());
        return coiRepository.save(coi);
    }

    public void delete(Long id) {
        Coi coi = getById(id);
        coiRepository.delete(coi);
    }
}
