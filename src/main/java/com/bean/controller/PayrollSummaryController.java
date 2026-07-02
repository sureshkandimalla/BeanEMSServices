package com.bean.controller;

import com.bean.model.PayPeriod;
import com.bean.model.PayrollSummary;
import com.bean.repository.PayPeriodRepository;
import com.bean.repository.PayrollSummaryRepository;
import com.bean.service.PayrollSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@CrossOrigin(origins = {"http://beanems.s3-website-us-east-1.amazonaws.com", "http://localhost:3000", "http://localhost:4000"})
@RestController
@RequestMapping("/api/v1/payrollsummary")
public class PayrollSummaryController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(PayrollSummaryController.class);

    @Autowired
    private PayrollSummaryService payrollSummaryService;

    @Autowired
    private PayrollSummaryRepository payrollSummaryRepository;

    @Autowired
    private PayPeriodRepository payPeriodRepository;

    @GetMapping("/getAll")
    public List<PayrollSummary> getAll() {
        return payrollSummaryService.getAllPayrollSummaries();
    }

    @GetMapping("/getByEmployee")
    public List<PayrollSummary> getByEmployee(@RequestParam String employeeName) {
        return payrollSummaryService.getByEmployeeName(employeeName);
    }

    @GetMapping("/getByEmployeeId/{employeeId}")
    public List<PayrollSummary> getByEmployeeId(@PathVariable Long employeeId) {
        return payrollSummaryService.getByEmployeeId(employeeId);
    }

    @GetMapping("/getByDateRange")
    public List<PayrollSummary> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return payrollSummaryService.getByDateRange(startDate, endDate);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importCsv(@RequestParam("file") MultipartFile file) {
        try {
            List<PayrollSummary> saved = payrollSummaryService.importFromCsv(file);
            return ResponseEntity.ok("Successfully imported " + saved.size() + " payroll summary records.");
        } catch (Exception e) {
            logger.error("Failed to import payroll summary CSV", e);
            return ResponseEntity.badRequest().body("Import failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> deleteAll() {
        payrollSummaryRepository.deleteAll();
        return ResponseEntity.ok("All payroll summary records deleted.");
    }

    @GetMapping("/getPayPeriods")
    public List<PayPeriod> getPayPeriods() {
        return payPeriodRepository.findAll();
    }

    @PostMapping("/reprocessEmployee")
    public ResponseEntity<?> reprocessEmployee(@RequestParam String employeeName) {
        int updated = payrollSummaryService.reprocessEmployeeMatch(employeeName);
        return ResponseEntity.ok("Updated employeeId for " + updated + " records matching: " + employeeName);
    }

    @PostMapping("/fixPayPeriodDates")
    public ResponseEntity<?> fixPayPeriodDates() {
        List<PayPeriod> all = payPeriodRepository.findAll();

        List<Map<String, Object>> correct   = new ArrayList<>();
        List<Map<String, Object>> wrong     = new ArrayList<>();
        List<Map<String, Object>> corrupt   = new ArrayList<>();

        for (PayPeriod pp : all) {
            LocalDate payDate   = pp.getPayDate();
            LocalDate startDate = pp.getStartDate();
            LocalDate endDate   = pp.getEndDate();

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("payPeriodId", pp.getPayPeriodId());
            entry.put("payDate",     payDate);
            entry.put("startDate",   startDate);
            entry.put("endDate",     endDate);

            // Detect corrupt dates (year out of valid range)
            if (payDate == null || payDate.getYear() < 2020 || payDate.getYear() > 2030
                    || startDate == null || startDate.getYear() < 2000
                    || endDate == null   || endDate.getYear()   < 2000) {
                entry.put("issue", "CORRUPT - invalid date values");
                corrupt.add(entry);
                continue;
            }

            long diff = ChronoUnit.DAYS.between(startDate, endDate);
            entry.put("spanDays", diff + 1); // inclusive count
            if (diff == 13) {
                correct.add(entry);
            } else {
                entry.put("issue", diff < 13 ? "TOO SHORT (" + (diff+1) + " days)" : "TOO LONG (" + (diff+1) + " days)");
                wrong.add(entry);
            }
        }

        Map<String, Object> report = new LinkedHashMap<>();
        report.put("totalRecords",  all.size());
        report.put("correct_14day", correct.size());
        report.put("wrong_span",    wrong.size());
        report.put("corrupt",       corrupt.size());
        report.put("wrongRecords",  wrong);
        report.put("corruptRecords",corrupt);

        return ResponseEntity.ok(report);
    }
}
