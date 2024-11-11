package com.bean.service;

import com.bean.model.Bills;
import com.bean.model.Wage;
import com.bean.repository.BillsRepository;
import com.bean.repository.WageRepository;
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
