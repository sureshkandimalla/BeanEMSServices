package com.employeehub.service;

import com.employeehub.exception.EmployeeServiceException;
import com.employeehub.model.Assignment;
import com.employeehub.model.Customer;
import com.employeehub.model.Employee;
import com.employeehub.model.Project;
import com.employeehub.repository.AssignmentRepository;
import com.employeehub.repository.CustomerRepository;
import com.employeehub.repository.EmployeeRepository;
import com.employeehub.repository.ProjectRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Backs the "Master Data Load" page: one Excel template + one Excel import
 * per entity (Employees, Customers, Projects, Assignments), built on
 * the same Apache POI usage already established in
 * {@link PayrollSummaryService#importFromXlsxResource()} — header row, a
 * per-row try/catch so one bad row doesn't sink the batch, and a result that
 * reports what failed instead of throwing.
 *
 * Every import here calls straight into the same save methods the UI's own
 * onboarding forms use ({@link EmployeeService#saveEmployeeDetails},
 * {@link CustomerService#saveCustomer}, {@link ProjectService#saveProject}),
 * so an imported row is indistinguishable from one entered by hand — same ID
 * assignment rules, same defaults.
 */
@Service
public class MasterDataLoadService {

    private static final Logger logger = LoggerFactory.getLogger(MasterDataLoadService.class);

    private static final String EXAMPLE_MARKER = "Delete this example row before uploading →";

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;

    public static class RowError {
        private final int row;
        private final String reason;

        public RowError(int row, String reason) {
            this.row = row;
            this.reason = reason;
        }

        public int getRow() { return row; }
        public String getReason() { return reason; }
    }

    public static class ImportResult {
        private final int imported;
        private final List<RowError> errors;

        public ImportResult(int imported, List<RowError> errors) {
            this.imported = imported;
            this.errors = errors;
        }

        public int getImported() { return imported; }
        public List<RowError> getErrors() { return errors; }
    }

    // ==================================================================
    // Templates
    // ==================================================================

    public byte[] buildEmployeeTemplate() throws IOException {
        String[] headers = {
                "First Name", "Last Name", "Email", "Phone", "DOB (yyyy-MM-dd)", "SSN", "Gender",
                "Designation", "Employment Type", "Tax Term", "Visa", "Status",
                "Start Date (yyyy-MM-dd)", "End Date (yyyy-MM-dd)", "Location",
                "Street Address", "City", "State", "Zip Code", "Country", "Referred By",
                "Everify Status", "I9", "PAF", "Company Name",
        };
        Object[] example = {
                "John", "Doe", "john.doe@example.com", "+1 (555) 123-4567", "1990-05-15",
                "123-45-6789", "M", "Software Developer", "Full-Time", "W2", "H1B", "Active",
                "2026-01-15", "", "Dallas", "123 Main St", "Dallas", "TX", "75201", "USA",
                "Jane Smith", "Completed", "Completed", "Completed", "Intellan Technologies LLC",
        };
        Map<String, String[]> reference = new LinkedHashMap<>();
        reference.put("Gender", new String[]{"M", "F"});
        reference.put("Employment Type", new String[]{"Full-Time", "C2C", "Part-Time", "Hourly"});
        reference.put("Tax Term", new String[]{"W2", "1099", "C2C", "Other"});
        reference.put("Visa", new String[]{"H1B", "OPT", "OPT EAD", "STEM EAD", "H4 EAD", "Citizen", "GC", "L1", "E3"});
        reference.put("Status", new String[]{"Active", "Inactive", "Bench", "OnBoarding", "NewHires"});
        reference.put("Everify Status", new String[]{"Completed", "In Process", "Removed", "Not Completed", "Drafted"});
        reference.put("Company Name", new String[]{"Intellan Technologies LLC", "Code9 LLC", "Referral"});
        return buildTemplateWorkbook(headers, example, reference);
    }

    public byte[] buildCustomerTemplate() throws IOException {
        String[] headers = {
                "Customer Company Name", "Customer Contact Name", "EIN", "Phone", "Email", "Website",
                "Start Date (yyyy-MM-dd)", "End Date (yyyy-MM-dd)", "Street Address",
                "Street Address 2", "City", "State", "Zip Code", "Country",
        };
        Object[] example = {
                "Acme Staffing Inc", "Robert Johnson", "12-3456789", "+1 (555) 987-6543",
                "contact@acmestaffing.com", "www.acmestaffing.com", "2026-01-01", "",
                "500 Corporate Dr", "Suite 200", "Chicago", "IL", "60601", "USA",
        };
        return buildTemplateWorkbook(headers, example, Map.of());
    }

    public byte[] buildProjectTemplate() throws IOException {
        String[] headers = {
                "Project Name", "Customer Company Name", "Employee Full Name", "Client",
                "Start Date (yyyy-MM-dd)", "End Date (yyyy-MM-dd)", "Bill Rate",
                "Invoice Term", "Payment Term", "Week Start Day", "Status",
        };
        Object[] example = {
                "Acme-Data Platform", "Acme Staffing Inc", "John Doe", "Acme Corp", "2026-01-15",
                "", 85.0, "Weekly", "Net 30", "Monday", "Active",
        };
        Map<String, String[]> reference = new LinkedHashMap<>();
        reference.put("Customer Company Name", new String[]{"must exactly match an already-loaded Customer's Customer Company Name"});
        reference.put("Employee Full Name", new String[]{"\"First Last\" — must exactly match an already-loaded Employee"});
        reference.put("Invoice Term", new String[]{"Weekly", "Biweekly", "Monthly", "Special", "Semi-Monthly", "Once in 4 Weeks"});
        reference.put("Week Start Day", new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});
        reference.put("Status", new String[]{"Active", "Inactive", "Hold", "OnBoarding"});
        return buildTemplateWorkbook(headers, example, reference);
    }

    public byte[] buildAssignmentTemplate() throws IOException {
        String[] headers = {
                "Employee Full Name", "Project Name", "Customer Company Name (optional, to disambiguate)",
                "Assignment Type", "Assignment Tax Type", "Wage", "Start Date (yyyy-MM-dd)",
                "End Date (yyyy-MM-dd)", "Description",
        };
        Object[] example = {
                "John Doe", "Acme-Data Platform", "Acme Staffing Inc", "EMP_PAY", "W2", 65.0,
                "2026-01-15", "", "Primary employee pay rate for this project",
        };
        Map<String, String[]> reference = new LinkedHashMap<>();
        reference.put("Employee Full Name", new String[]{"\"First Last\" — must exactly match an already-loaded Employee"});
        reference.put("Project Name", new String[]{"must exactly match an already-loaded Project"});
        reference.put("Assignment Type", new String[]{"BILLING", "REF", "EMP_PAY", "EMP_REF", "EMP_COMM", "COMM"});
        reference.put("Assignment Tax Type", new String[]{"W2", "1099", "C2C"});
        return buildTemplateWorkbook(headers, example, reference);
    }

    private byte[] buildTemplateWorkbook(String[] headers, Object[] exampleRow, Map<String, String[]> referenceValues)
            throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            CellStyle headerStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            headerStyle.setFont(boldFont);

            Sheet sheet = workbook.createSheet("Template");
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            Row exampleRowObj = sheet.createRow(1);
            for (int i = 0; i < exampleRow.length; i++) {
                Object value = exampleRow[i];
                if (value == null) continue;
                Cell cell = exampleRowObj.createCell(i);
                if (value instanceof Number number) {
                    cell.setCellValue(number.doubleValue());
                } else {
                    cell.setCellValue(value.toString());
                }
            }
            exampleRowObj.createCell(exampleRow.length + 1).setCellValue(EXAMPLE_MARKER);

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            if (!referenceValues.isEmpty()) {
                Sheet refSheet = workbook.createSheet("Reference Values");
                Row refHeader = refSheet.createRow(0);
                Cell fieldHeader = refHeader.createCell(0);
                fieldHeader.setCellValue("Field");
                fieldHeader.setCellStyle(headerStyle);
                Cell valuesHeader = refHeader.createCell(1);
                valuesHeader.setCellValue("Valid Values");
                valuesHeader.setCellStyle(headerStyle);

                int r = 1;
                for (Map.Entry<String, String[]> entry : referenceValues.entrySet()) {
                    Row row = refSheet.createRow(r++);
                    row.createCell(0).setCellValue(entry.getKey());
                    row.createCell(1).setCellValue(String.join(", ", entry.getValue()));
                }
                refSheet.autoSizeColumn(0);
                refSheet.autoSizeColumn(1);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    // ==================================================================
    // Imports
    // ==================================================================

    public ImportResult importEmployees(MultipartFile file) throws IOException {
        int imported = 0;
        List<RowError> errors = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isExampleRow(row) || isBlankRow(row)) continue;
                int excelRow = row.getRowNum() + 1;
                try {
                    String firstName = getStringValue(row.getCell(0));
                    String lastName = getStringValue(row.getCell(1));
                    if (firstName.isEmpty() || lastName.isEmpty()) continue; // trailing blank rows

                    com.employeehub.domain.Employee dto = new com.employeehub.domain.Employee(
                            null,
                            getStringValue(row.getCell(7)),   // designation
                            getStringValue(row.getCell(4)),   // dob
                            getStringValue(row.getCell(5)),   // ssn
                            getStringValue(row.getCell(3)),   // phoneNumber
                            getStringValue(row.getCell(2)),   // emailId
                            getStringValue(row.getCell(8)),   // employmentType
                            null,                              // employeeType (not persisted by saveEmployeeDetails)
                            null,                              // vendorId (no column in the import sheet)
                            firstName,
                            lastName,
                            requireNonBlank(getStringValue(row.getCell(6)), "Gender"),
                            getLocalDateValue(row.getCell(12)), // startDate
                            getLocalDateValue(row.getCell(13)), // endDate
                            getStringValue(row.getCell(20)),  // referredBy
                            getStringValue(row.getCell(9)),   // taxTerms
                            getStringValue(row.getCell(10)),  // visa
                            getStringValue(row.getCell(15)),  // streetAddress
                            "",                                 // address_line_2
                            getStringValue(row.getCell(16)),  // city
                            getStringValue(row.getCell(17)),  // state
                            requireNonBlank(getStringValue(row.getCell(19)), "Country"),
                            getStringValue(row.getCell(18)),  // zipCode
                            getStringValue(row.getCell(11)),  // status
                            getStringValue(row.getCell(14)),  // location
                            null, null,                        // income, expense (unused by saveEmployeeDetails)
                            getStringValue(row.getCell(21)),  // everifyStatus
                            getStringValue(row.getCell(22)),  // i9
                            getStringValue(row.getCell(23)),  // PAF
                            "",                                 // insurance
                            0f,                                 // annualPay (unused by saveEmployeeDetails)
                            getStringValue(row.getCell(24))   // companyName
                    );

                    employeeService.saveEmployeeDetails(dto);
                    imported++;
                } catch (EmployeeServiceException e) {
                    errors.add(new RowError(excelRow, e.getMessage()));
                } catch (Exception e) {
                    logger.warn("Skipping employee row {}: {}", excelRow, e.getMessage());
                    errors.add(new RowError(excelRow, e.getMessage()));
                }
            }
        }

        return new ImportResult(imported, errors);
    }

    public ImportResult importCustomers(MultipartFile file) throws IOException {
        int imported = 0;
        List<RowError> errors = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isExampleRow(row) || isBlankRow(row)) continue;
                int excelRow = row.getRowNum() + 1;
                try {
                    String customerCompanyName = getStringValue(row.getCell(0));
                    if (customerCompanyName.isEmpty()) continue;

                    com.employeehub.domain.Customer dto = new com.employeehub.domain.Customer(
                            null,
                            getStringValue(row.getCell(1)),  // customerName
                            getStringValue(row.getCell(2)),  // ein
                            getStringValue(row.getCell(3)),  // phone
                            getStringValue(row.getCell(4)),  // emailId
                            getLocalDateValue(row.getCell(6)),  // startDate
                            getLocalDateValue(row.getCell(7)),  // endDate
                            customerCompanyName,
                            getStringValue(row.getCell(5)),  // webSite
                            getStringValue(row.getCell(8)),  // streetAddress
                            getStringValue(row.getCell(9)),  // streetAddress2
                            getStringValue(row.getCell(10)), // city
                            getStringValue(row.getCell(11)), // state
                            getStringValue(row.getCell(12)), // zipCode
                            getStringValue(row.getCell(13))  // country
                    );

                    customerService.saveCustomer(dto);
                    imported++;
                } catch (Exception e) {
                    logger.warn("Skipping customer row {}: {}", excelRow, e.getMessage());
                    errors.add(new RowError(excelRow, e.getMessage()));
                }
            }
        }

        return new ImportResult(imported, errors);
    }

    public ImportResult importProjects(MultipartFile file) throws IOException {
        int imported = 0;
        List<RowError> errors = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isExampleRow(row) || isBlankRow(row)) continue;
                int excelRow = row.getRowNum() + 1;
                try {
                    String projectName = getStringValue(row.getCell(0));
                    String customerCompanyName = getStringValue(row.getCell(1));
                    String employeeFullName = getStringValue(row.getCell(2));
                    if (projectName.isEmpty()) continue;

                    Customer customer = customerRepository.findByCustomerCompanyNameIgnoreCase(customerCompanyName)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Customer '" + customerCompanyName + "' not found — load Customers first"));
                    Employee employee = findEmployeeByFullName(employeeFullName)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Employee '" + employeeFullName + "' not found — load Employees first"));

                    com.employeehub.domain.Project dto = new com.employeehub.domain.Project();
                    dto.setProjectName(projectName);
                    dto.setCustomerId(customer.getCustomerId());
                    dto.setEmployeeId(employee.getEmployeeId());
                    dto.setClient(getStringValue(row.getCell(3)));
                    dto.setStartDate(getLocalDateValue(row.getCell(4)));
                    dto.setEndDate(getLocalDateValue(row.getCell(5)));
                    dto.setBillRate((float) getNumericValue(row.getCell(6)));
                    dto.setInvoiceTerm(resolveInvoiceTermCode(getStringValue(row.getCell(7))));
                    dto.setPaymentTerm(getStringValue(row.getCell(8)));
                    String weekStartDay = getStringValue(row.getCell(9));
                    dto.setWeekStartDay(weekStartDay.isEmpty() ? "MONDAY" : weekStartDay.trim().toUpperCase());
                    dto.setStatus(getStringValue(row.getCell(10)));

                    var response = projectService.saveProject(dto);
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        throw new IllegalArgumentException(response.getBody());
                    }
                    imported++;
                } catch (Exception e) {
                    logger.warn("Skipping project row {}: {}", excelRow, e.getMessage());
                    errors.add(new RowError(excelRow, e.getMessage()));
                }
            }
        }

        return new ImportResult(imported, errors);
    }

    public ImportResult importAssignments(MultipartFile file) throws IOException {
        int imported = 0;
        List<RowError> errors = new ArrayList<>();

        try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0 || isExampleRow(row) || isBlankRow(row)) continue;
                int excelRow = row.getRowNum() + 1;
                try {
                    String employeeFullName = getStringValue(row.getCell(0));
                    String projectName = getStringValue(row.getCell(1));
                    String customerCompanyName = getStringValue(row.getCell(2));
                    if (employeeFullName.isEmpty() || projectName.isEmpty()) continue;

                    Employee employee = findEmployeeByFullName(employeeFullName)
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Employee '" + employeeFullName + "' not found — load Employees first"));
                    Project project = findProjectByName(projectName, customerCompanyName);

                    Assignment assignment = new Assignment();
                    assignment.setProjectId(project.getProjectId());
                    assignment.setEmployeeId(employee.getEmployeeId());
                    assignment.setAssignmentType(getStringValue(row.getCell(3)));
                    assignment.setAssignmentTaxType(getStringValue(row.getCell(4)));
                    assignment.setWage((float) getNumericValue(row.getCell(5)));
                    assignment.setStartDate(getLocalDateValue(row.getCell(6)));
                    assignment.setEndDate(getLocalDateValue(row.getCell(7)));
                    assignment.setDescription(getStringValue(row.getCell(8)));

                    // Same defaulting/status rule as AssignmentController#createAssignment.
                    if (assignment.getEndDate() == null) {
                        assignment.setEndDate(LocalDate.of(9999, 12, 31));
                    }
                    assignment.setStatus(assignment.getEndDate().isAfter(LocalDate.now()) ? "Active" : "Inactive");

                    assignmentRepository.save(assignment);
                    imported++;
                } catch (Exception e) {
                    logger.warn("Skipping assignment row {}: {}", excelRow, e.getMessage());
                    errors.add(new RowError(excelRow, e.getMessage()));
                }
            }
        }

        return new ImportResult(imported, errors);
    }

    // ==================================================================
    // Shared lookups & cell helpers
    // ==================================================================

    // "First Last" — everything before the last space is the first name, the
    // last space-separated token is the last name. Matches how names are
    // entered on this same template (not the "Last, First" convention the
    // Payroll Summary import uses for its own, different source data).
    private Optional<Employee> findEmployeeByFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) return Optional.empty();
        String trimmed = fullName.trim();
        int lastSpace = trimmed.lastIndexOf(' ');
        if (lastSpace == -1) return Optional.empty();
        String firstName = trimmed.substring(0, lastSpace).trim();
        String lastName = trimmed.substring(lastSpace + 1).trim();
        return employeeRepository.findByLastNameIgnoreCaseAndFirstNameIgnoreCase(lastName, firstName);
    }

    private Project findProjectByName(String projectName, String customerCompanyNameOrNull) {
        List<Project> matches = projectRepository.findByProjectNameIgnoreCase(projectName);
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("Project '" + projectName + "' not found — load Projects first");
        }
        if (matches.size() == 1) {
            return matches.get(0);
        }
        if (customerCompanyNameOrNull != null && !customerCompanyNameOrNull.isBlank()) {
            return matches.stream()
                    .filter(p -> p.getCustomer() != null
                            && customerCompanyNameOrNull.equalsIgnoreCase(p.getCustomer().getCustomerCompanyName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Multiple projects named '" + projectName + "' — none matched customer '" + customerCompanyNameOrNull + "'"));
        }
        throw new IllegalArgumentException(
                "Multiple projects named '" + projectName + "' — add a Customer Company Name to disambiguate");
    }

    private String resolveInvoiceTermCode(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String trimmed = raw.trim();
        // Already a numeric code (1-6) — pass through as-is.
        if (trimmed.matches("[1-6]")) return trimmed;
        return switch (trimmed.toLowerCase()) {
            case "weekly" -> "1";
            case "biweekly" -> "2";
            case "monthly" -> "3";
            case "special" -> "4";
            case "semi-monthly", "semi monthly" -> "5";
            case "once in 4 weeks" -> "6";
            default -> throw new IllegalArgumentException("Unrecognized Invoice Term: '" + raw + "'");
        };
    }

    private String requireNonBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value;
    }

    // Flags the template's own filled-in example row so it's silently
    // skipped if a user forgets to delete it before uploading.
    private boolean isExampleRow(Row row) {
        for (Cell cell : row) {
            if (cell.getCellType() == CellType.STRING && EXAMPLE_MARKER.equals(cell.getStringCellValue())) {
                return true;
            }
        }
        return false;
    }

    private boolean isBlankRow(Row row) {
        for (Cell cell : row) {
            if (getStringValue(cell).length() > 0) return false;
        }
        return true;
    }

    private String getStringValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate().toString();
            }
            double value = cell.getNumericCellValue();
            if (value == Math.floor(value)) {
                return String.valueOf((long) value);
            }
            return String.valueOf(value);
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
            if (cleaned.isEmpty()) return 0d;
            try {
                return Double.parseDouble(cleaned);
            } catch (NumberFormatException e) {
                return 0d;
            }
        }
        return 0d;
    }

    private LocalDate getLocalDateValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }
        String raw = getStringValue(cell);
        if (raw.isEmpty()) return null;
        try {
            return LocalDate.parse(raw.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
