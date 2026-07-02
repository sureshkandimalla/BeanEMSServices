package com.bean.service;

import com.bean.model.PayrollSummary;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.PayPeriodRepository;
import com.bean.repository.PayrollSummaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayrollSummaryService {

    private static final Logger logger = LoggerFactory.getLogger(PayrollSummaryService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    @Autowired
    private PayrollSummaryRepository payrollSummaryRepository;

    @Autowired
    private PayPeriodRepository payPeriodRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<PayrollSummary> getAllPayrollSummaries() {
        return enrichWithPayPeriodDates(payrollSummaryRepository.findAll());
    }

    public List<PayrollSummary> getByEmployeeName(String employeeName) {
        return enrichWithPayPeriodDates(payrollSummaryRepository.findByEmployeeNameContainingIgnoreCase(employeeName));
    }

    public List<PayrollSummary> getByEmployeeId(Long employeeId) {
        return enrichWithPayPeriodDates(payrollSummaryRepository.findByEmployeeId(employeeId));
    }

    public List<PayrollSummary> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return enrichWithPayPeriodDates(payrollSummaryRepository.findByCheckDateBetween(startDate, endDate));
    }

    public int reprocessEmployeeMatch(String employeeName) {
        List<PayrollSummary> records = payrollSummaryRepository.findByEmployeeNameContainingIgnoreCase(employeeName);
        int updated = 0;
        for (PayrollSummary ps : records) {
            if (ps.getEmployeeId() == null && ps.getEmployeeName() != null && ps.getEmployeeName().contains(",")) {
                String[] parts = ps.getEmployeeName().split(",", 2);
                String lastName  = parts[0].trim();
                String firstName = parts[1].trim();
                var emp = employeeRepository.findByLastNameIgnoreCaseAndFirstNameIgnoreCase(lastName, firstName);
                if (emp.isPresent()) {
                    ps.setEmployeeId(emp.get().getEmployeeId());
                    if (emp.get().getCompanyName() != null && !emp.get().getCompanyName().isEmpty()) {
                        ps.setDepartment(emp.get().getCompanyName());
                    }
                    payrollSummaryRepository.save(ps);
                    updated++;
                }
            }
        }
        return updated;
    }

    private List<PayrollSummary> enrichWithPayPeriodDates(List<PayrollSummary> records) {
        records.forEach(ps -> {
            if (ps.getPayPeriodId() != null) {
                payPeriodRepository.findById(ps.getPayPeriodId()).ifPresent(pp -> {
                    ps.setPayPeriodStartDate(pp.getStartDate());
                    ps.setPayPeriodEndDate(pp.getEndDate());
                });
            }
        });
        return records;
    }

    public List<PayrollSummary> importFromCsv(MultipartFile file) throws Exception {
        List<PayrollSummary> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                // Skip the header row
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                if (line.trim().isEmpty()) continue;

                String[] cols = parseCsvLine(line);
                if (cols.length < 12) continue;

                try {
                    PayrollSummary ps = new PayrollSummary();
                    ps.setPayFrequency(cols[0].trim());
                    ps.setDepartment(cols[1].trim());
                    LocalDate checkDate = LocalDate.parse(cols[2].trim(), DATE_FORMATTER);
                    ps.setCheckDate(checkDate);
                    String rawName = cols[3].trim().replaceAll("^\"|\"$", "").trim();
                    ps.setEmployeeName(rawName);

                    // Match PayPeriod by checkDate == payDate
                    payPeriodRepository.findByPayDate(checkDate)
                            .ifPresent(pp -> ps.setPayPeriodId(pp.getPayPeriodId()));

                    // Match Employee by "LastName, FirstName" format in CSV
                    // e.g. "Kandimalla, Suresh R" -> lastName="Kandimalla", firstName="Suresh R"
                    if (rawName.contains(",")) {
                        String[] nameParts = rawName.split(",", 2);
                        String lastName = nameParts[0].trim();
                        String firstName = nameParts[1].trim();
                        employeeRepository.findByLastNameIgnoreCaseAndFirstNameIgnoreCase(lastName, firstName)
                                .ifPresent(emp -> {
                                    ps.setEmployeeId(emp.getEmployeeId());
                                    if (emp.getCompanyName() != null && !emp.getCompanyName().isEmpty()) {
                                        ps.setDepartment(emp.getCompanyName());
                                    }
                                });
                    }

                    ps.setHours(parseFloat(cols[4]));
                    ps.setTotalPaid(parseFloat(cols[5]));
                    ps.setTaxWithheld(parseFloat(cols[6]));
                    ps.setDeductions(parseFloat(cols[7]));
                    ps.setNetPay(parseFloat(cols[8]));
                    ps.setPaymentDetails(cols[9].trim().replaceAll("^\"|\"$", "").trim());
                    ps.setEmployerLiability(parseFloat(cols[10]));
                    ps.setTotalExpenses(parseFloat(cols[11]));
                    records.add(ps);
                } catch (Exception e) {
                    logger.warn("Skipping row due to parse error: {} | Error: {}", line, e.getMessage());
                }
            }
        }

        return payrollSummaryRepository.saveAll(records);
    }

    private float parseFloat(String value) {
        if (value == null) return 0f;
        // Remove $, commas, quotes, spaces, and handle $- as 0
        String cleaned = value.replaceAll("[\",\\$\\s]", "").replace("-", "0").trim();
        if (cleaned.isEmpty()) return 0f;
        try {
            return Float.parseFloat(cleaned);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * Parses a CSV line respecting quoted fields (fields may contain commas inside quotes).
     */
    private String[] parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
                current.append(c);
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result.toArray(new String[0]);
    }
}
