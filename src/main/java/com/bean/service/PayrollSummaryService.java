package com.bean.service;

import com.bean.model.Employee;
import com.bean.model.PayrollSummary;
import com.bean.repository.EmployeeRepository;
import com.bean.repository.PayPeriodRepository;
import com.bean.repository.PayrollSummaryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

@Service
public class PayrollSummaryService {

    private static final Logger logger = LoggerFactory.getLogger(PayrollSummaryService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");
    private static final DateTimeFormatter YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

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

    /**
     * Result of {@link #importFromXlsxResource()}: the saved records plus any
     * employee_name values from the sheet that couldn't be matched to an
     * Employee (those rows are still imported, just with a null employeeId,
     * matching the same "leave it for later reconciliation" approach as
     * {@link #reprocessEmployeeMatch(String)}).
     */
    public static class XlsxImportResult {
        public final List<PayrollSummary> saved;
        public final List<String> unmatchedEmployeeNames;

        public XlsxImportResult(List<PayrollSummary> saved, List<String> unmatchedEmployeeNames) {
            this.saved = saved;
            this.unmatchedEmployeeNames = unmatchedEmployeeNames;
        }
    }

    /**
     * Imports the bundled resources/data/IntellanPayroll.xlsx "Payroll
     * Summary" sheet. Unlike {@link #importFromCsv}, which expects an
     * uploaded file laid out for direct app use, this sheet is a raw export
     * (payroll_summary_id, check_date, pay_frequency, Department,
     * employee_name, hours, total_paid, tax_withheld, Deductions, net_pay,
     * payment_details, employer_liability, total_expenses, last_updated,
     * pay_period_id, employee_id) where the last three columns are blank in
     * the source file — this method derives them itself:
     *   - payPeriodId = the check date as a literal yyyyMMdd number (e.g.
     *     2025-04-15 -> 20250415), not a PayPeriod table lookup.
     *   - employeeId = resolved from "LastName, FirstName" in employee_name
     *     against the Employee table.
     * The source payroll_summary_id column is ignored — the DB assigns its
     * own identity value for every imported row.
     */
    public XlsxImportResult importFromXlsxResource() throws Exception {
        List<PayrollSummary> records = new ArrayList<>();
        List<String> unmatchedNames = new ArrayList<>();

        try (InputStream in = getClass().getResourceAsStream("/data/IntellanPayroll.xlsx");
             XSSFWorkbook workbook = new XSSFWorkbook(in)) {

            Sheet sheet = workbook.getSheet("Payroll Summary");
            if (sheet == null) {
                sheet = workbook.getSheetAt(workbook.getNumberOfSheets() - 1);
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // header

                String rawCheckDate = getStringValue(row.getCell(1));
                String rawName = getStringValue(row.getCell(4));
                if (rawCheckDate.isEmpty() || rawName.isEmpty()) continue; // trailing blank rows

                try {
                    PayrollSummary ps = new PayrollSummary();

                    LocalDate checkDate = LocalDate.parse(rawCheckDate.trim(), DATE_FORMATTER);
                    ps.setCheckDate(checkDate);
                    ps.setPayFrequency(getStringValue(row.getCell(2)));
                    ps.setDepartment(getStringValue(row.getCell(3)));
                    ps.setEmployeeName(rawName);
                    ps.setHours((float) getNumericValue(row.getCell(5)));
                    ps.setTotalPaid((float) getNumericValue(row.getCell(6)));
                    ps.setTaxWithheld((float) getNumericValue(row.getCell(7)));
                    ps.setDeductions((float) getNumericValue(row.getCell(8)));
                    ps.setNetPay((float) getNumericValue(row.getCell(9)));
                    ps.setPaymentDetails(getStringValue(row.getCell(10)));
                    ps.setEmployerLiability((float) getNumericValue(row.getCell(11)));
                    ps.setTotalExpenses((float) getNumericValue(row.getCell(12)));

                    // pay_period_id = literal yyyyMMdd of the check date.
                    ps.setPayPeriodId(Long.parseLong(checkDate.format(YYYYMMDD_FORMATTER)));

                    // employee_id resolved from "LastName, FirstName".
                    if (rawName.contains(",")) {
                        String[] parts = rawName.split(",", 2);
                        String lastName = parts[0].trim();
                        String firstName = parts[1].trim();
                        var emp = employeeRepository.findByLastNameIgnoreCaseAndFirstNameIgnoreCase(lastName, firstName);
                        if (emp.isPresent()) {
                            Employee employee = emp.get();
                            ps.setEmployeeId(employee.getEmployeeId());
                            if (employee.getCompanyName() != null && !employee.getCompanyName().isEmpty()) {
                                ps.setDepartment(employee.getCompanyName());
                            }
                        } else {
                            unmatchedNames.add(rawName);
                            logger.warn("No employee match for payroll row (row {}): '{}'", row.getRowNum(), rawName);
                        }
                    } else {
                        unmatchedNames.add(rawName);
                        logger.warn("employee_name not in 'Last, First' format (row {}): '{}'", row.getRowNum(), rawName);
                    }

                    records.add(ps);
                } catch (Exception e) {
                    logger.warn("Skipping xlsx row {} due to parse error: {}", row.getRowNum(), e.getMessage());
                }
            }
        }

        List<PayrollSummary> saved = payrollSummaryRepository.saveAll(records);
        return new XlsxImportResult(saved, unmatchedNames);
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.toString().trim();
    }

    private double getNumericValue(Cell cell) {
        if (cell == null) return 0d;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        if (cell.getCellType() == CellType.STRING) {
            String cleaned = cell.getStringCellValue().replaceAll("[\",\\$\\s]", "").trim();
            if (cleaned.isEmpty() || cleaned.equals("-")) return 0d;
            try {
                return Double.parseDouble(cleaned);
            } catch (NumberFormatException e) {
                return 0d;
            }
        }
        return 0d;
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
