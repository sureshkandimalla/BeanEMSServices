package com.bean.service;

import com.bean.model.Employee;
import com.bean.model.HealthInsurance;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.HealthInsuranceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HealthInsuranceService {

    private static final Logger logger = LoggerFactory.getLogger(HealthInsuranceService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yy");

    @Autowired
    private HealthInsuranceRepository healthInsuranceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<HealthInsurance> getAll() {
        return enrichWithEmployeeName(healthInsuranceRepository.findAll());
    }

    public List<HealthInsurance> getByEmployeeName(String employeeName) {
        return enrichWithEmployeeName(healthInsuranceRepository.findByEmployeeNameContainingIgnoreCase(employeeName));
    }

    public List<HealthInsurance> getByEmployeeId(Long employeeId) {
        return enrichWithEmployeeName(healthInsuranceRepository.findByEmployeeId(employeeId));
    }

    public List<HealthInsurance> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return enrichWithEmployeeName(healthInsuranceRepository.findByDateOfBillBetween(startDate, endDate));
    }

    /**
     * Displays the Employee table's name (authoritative, properly cased) for
     * rows matched to an employee; rows with no employeeId keep the raw
     * employee_name as it came from the uploaded file.
     */
    private List<HealthInsurance> enrichWithEmployeeName(List<HealthInsurance> records) {
        records.forEach(hi -> {
            if (hi.getEmployeeId() != null) {
                employeeRepository.findById(hi.getEmployeeId()).ifPresent(emp -> {
                    String fullName = (emp.getFirstName() + " " + emp.getLastName()).trim();
                    hi.setEmployeeName(fullName);
                });
            }
        });
        return records;
    }

    public static class CsvImportResult {
        public final List<HealthInsurance> saved;
        public final List<String> unmatchedEmployeeNames;

        public CsvImportResult(List<HealthInsurance> saved, List<String> unmatchedEmployeeNames) {
            this.saved = saved;
            this.unmatchedEmployeeNames = unmatchedEmployeeNames;
        }
    }

    /**
     * Imports the bundled resources/data/HealthInsurance.csv, laid out as:
     * Group Id, Date of Bill, Due Date, Employee Name, UMI, Claim Prefund,
     * Specific Stop Loss, Aggregate Stop Loss, Admin Fee, Total, Comment.
     */
    public CsvImportResult importFromResource() throws Exception {
        try (InputStream in = getClass().getResourceAsStream("/data/HealthInsurance.csv")) {
            return parseCsv(in);
        }
    }

    public CsvImportResult importFromCsv(MultipartFile file) throws Exception {
        try (InputStream in = file.getInputStream()) {
            return parseCsv(in);
        }
    }

    private CsvImportResult parseCsv(InputStream in) throws Exception {
        List<HealthInsurance> records = new ArrayList<>();
        List<String> unmatchedNames = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            boolean headerSkipped = false;

            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                if (line.trim().isEmpty()) continue;

                String[] cols = parseCsvLine(line);
                if (cols.length < 10) continue;

                try {
                    HealthInsurance hi = new HealthInsurance();
                    hi.setGroupId(cols[0].trim());
                    hi.setDateOfBill(LocalDate.parse(cols[1].trim(), DATE_FORMATTER));
                    hi.setDueDate(LocalDate.parse(cols[2].trim(), DATE_FORMATTER));

                    String rawName = cols[3].trim();
                    hi.setEmployeeName(rawName);

                    hi.setUmi(cols[4].trim());
                    hi.setClaimPrefund(parseFloat(cols[5]));
                    hi.setSpecificStopLoss(parseFloat(cols[6]));
                    hi.setAggregateStopLoss(parseFloat(cols[7]));
                    hi.setAdminFee(parseFloat(cols[8]));
                    hi.setTotal(parseFloat(cols[9]));
                    hi.setComment(cols.length > 10 ? cols[10].trim() : "");

                    matchEmployee(rawName).ifPresentOrElse(
                            emp -> hi.setEmployeeId(emp.getEmployeeId()),
                            () -> {
                                unmatchedNames.add(rawName);
                                logger.warn("No employee match for health insurance row: '{}'", rawName);
                            });

                    records.add(hi);
                } catch (Exception e) {
                    logger.warn("Skipping health insurance row due to parse error: {} | Error: {}", line, e.getMessage());
                }
            }
        }

        List<HealthInsurance> saved = healthInsuranceRepository.saveAll(records);
        return new CsvImportResult(saved, unmatchedNames);
    }

    /**
     * Matches an employee_name like "THARUN-REDDY GANGASAN" or "NAVEEN KUMAR-PAKA"
     * against the Employee table. The name is tokenized on whitespace and hyphens;
     * the first token is treated as the first name, the last token as the last
     * name, and any tokens in between as a middle name (not used for matching).
     * Last name is matched first since it is the more selective, less-abbreviated
     * field; when several employees share that last name, the first-name token is
     * used (case-insensitively, prefix match, since the Employee record's first
     * name may carry extra words, e.g. "Naveen Kumar") to pick the right one.
     */
    private Optional<Employee> matchEmployee(String rawName) {
        String[] tokens = rawName.trim().split("[\\s-]+");
        if (tokens.length < 2) {
            return Optional.empty();
        }

        String firstNameToken = tokens[0];
        String lastNameToken = tokens[tokens.length - 1];

        List<Employee> candidates = employeeRepository.findByLastNameIgnoreCase(lastNameToken);
        if (candidates.isEmpty()) {
            return Optional.empty();
        }
        if (candidates.size() == 1) {
            return Optional.of(candidates.get(0));
        }

        return candidates.stream()
                .filter(emp -> emp.getFirstName() != null
                        && emp.getFirstName().trim().toLowerCase().startsWith(firstNameToken.toLowerCase()))
                .findFirst();
    }

    /**
     * Re-attempts employee matching for every existing HealthInsurance record
     * that has no employeeId, using the current {@link #matchEmployee} logic.
     * Useful after fixing/enriching Employee data without re-uploading the CSV.
     */
    public int reprocessUnmatched() {
        List<HealthInsurance> unmatched = healthInsuranceRepository.findAll().stream()
                .filter(hi -> hi.getEmployeeId() == null)
                .toList();

        int updated = 0;
        for (HealthInsurance hi : unmatched) {
            Optional<Employee> match = matchEmployee(hi.getEmployeeName());
            if (match.isPresent()) {
                hi.setEmployeeId(match.get().getEmployeeId());
                healthInsuranceRepository.save(hi);
                updated++;
            }
        }
        return updated;
    }

    private float parseFloat(String value) {
        if (value == null) return 0f;
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
