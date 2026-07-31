package com.employeehub.service;

import com.employeehub.model.Bills;
import com.employeehub.model.Wage;
import com.employeehub.repository.BillsRepository;
import com.employeehub.repository.WageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WageService {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(WageService.class);
	
	@Autowired
    private WageRepository wageRepository;
	

}
