package com.bean;

import com.bean.controller.PayrollSummaryController;
import com.bean.model.PayrollSummary;
import com.bean.service.PayrollSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PayrollSummaryControllerTest {

    @Mock
    private PayrollSummaryService payrollSummaryService;

    @InjectMocks
    private PayrollSummaryController payrollSummaryController;

    private MockMvc mockMvc;

    private PayrollSummary sample1;
    private PayrollSummary sample2;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(payrollSummaryController).build();

        sample1 = new PayrollSummary();
        sample1.setPayrollSummaryId(1L);
        sample1.setEmployeeId(5L);
        sample1.setPayFrequency("Biweekly");
        sample1.setDepartment("CODE9 LLC");
        sample1.setCheckDate(LocalDate.of(2025, 12, 26));
        sample1.setEmployeeName("Katam, Manoj");
        sample1.setHours(80f);
        sample1.setTotalPaid(4076.81f);
        sample1.setTaxWithheld(886.24f);
        sample1.setDeductions(0f);
        sample1.setNetPay(3190.57f);
        sample1.setPaymentDetails("50007");
        sample1.setEmployerLiability(334.72f);
        sample1.setTotalExpenses(4411.53f);

        sample2 = new PayrollSummary();
        sample2.setPayrollSummaryId(2L);
        sample2.setPayFrequency("Biweekly");
        sample2.setDepartment("CODE9 LLC");
        sample2.setCheckDate(LocalDate.of(2025, 12, 12));
        sample2.setEmployeeName("Kandimalla, Sai Anuhya");
        sample2.setHours(80f);
        sample2.setTotalPaid(2000.00f);
        sample2.setTaxWithheld(161.60f);
        sample2.setDeductions(0f);
        sample2.setNetPay(1838.40f);
        sample2.setPaymentDetails("DD");
        sample2.setEmployerLiability(0f);
        sample2.setTotalExpenses(2000.00f);
    }

    @Test
    public void testGetAll() throws Exception {
        when(payrollSummaryService.getAllPayrollSummaries()).thenReturn(Arrays.asList(sample1, sample2));

        mockMvc.perform(get("/api/v1/payrollsummary/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].employeeName", is("Katam, Manoj")))
                .andExpect(jsonPath("$[1].employeeName", is("Kandimalla, Sai Anuhya")));
    }

    @Test
    public void testGetAll_EmptyList() throws Exception {
        when(payrollSummaryService.getAllPayrollSummaries()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/payrollsummary/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetByEmployee() throws Exception {
        when(payrollSummaryService.getByEmployeeName("Katam")).thenReturn(Arrays.asList(sample1));

        mockMvc.perform(get("/api/v1/payrollsummary/getByEmployee")
                        .param("employeeName", "Katam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employeeName", is("Katam, Manoj")))
                .andExpect(jsonPath("$[0].totalPaid", closeTo(4076.81, 0.01)));
    }

    @Test
    public void testGetByEmployee_NotFound() throws Exception {
        when(payrollSummaryService.getByEmployeeName("Unknown")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/payrollsummary/getByEmployee")
                        .param("employeeName", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetByDateRange() throws Exception {
        when(payrollSummaryService.getByDateRange(
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 31)))
                .thenReturn(Arrays.asList(sample1, sample2));

        mockMvc.perform(get("/api/v1/payrollsummary/getByDateRange")
                        .param("startDate", "2025-12-01")
                        .param("endDate", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].checkDate[0]", is(2025)))
                .andExpect(jsonPath("$[0].checkDate[1]", is(12)))
                .andExpect(jsonPath("$[1].checkDate[0]", is(2025)))
                .andExpect(jsonPath("$[1].checkDate[1]", is(12)));
    }

    @Test
    public void testImportCsv_Success() throws Exception {
        String csvContent = "Pay Frequency,Department,Check Date,Name,Hours,Total Paid,Tax Withheld,Deductions,Net Pay,Payment Details/Check No,Employer Liability,Total Expenses\n"
                + "Biweekly,CODE9 LLC,12/26/2025 ,\"Katam, Manoj\",80.00,\" $4,076.81 \", $886.24 , $-   ,\" $3,190.57 \",50007, $334.72 ,\" $4,411.53 \"";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "payroll.csv", "text/csv", csvContent.getBytes());

        when(payrollSummaryService.importFromCsv(any())).thenReturn(Arrays.asList(sample1));

        mockMvc.perform(multipart("/api/v1/payrollsummary/import").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Successfully imported 1 payroll summary records.")));
    }

    @Test
    public void testImportCsv_Failure() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "bad.csv", "text/csv", "bad data".getBytes());

        when(payrollSummaryService.importFromCsv(any())).thenThrow(new RuntimeException("Parse error"));

        mockMvc.perform(multipart("/api/v1/payrollsummary/import").file(mockFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Import failed: Parse error")));
    }

    @Test
    public void testGetByDateRange_NoResults() throws Exception {
        when(payrollSummaryService.getByDateRange(any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/payrollsummary/getByDateRange")
                        .param("startDate", "2020-01-01")
                        .param("endDate", "2020-01-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ── getByEmployeeId tests ────────────────────────────────────────────────

    @Test
    public void testGetByEmployeeId_ReturnsRecords() throws Exception {
        // sample1 already has employeeId=5 set in setUp()
        when(payrollSummaryService.getByEmployeeId(5L)).thenReturn(List.of(sample1));

        mockMvc.perform(get("/api/v1/payrollsummary/getByEmployeeId/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].employeeId", is(5)))
                .andExpect(jsonPath("$[0].employeeName", is("Katam, Manoj")))
                .andExpect(jsonPath("$[0].totalPaid", closeTo(4076.81, 0.01)))
                .andExpect(jsonPath("$[0].netPay",    closeTo(3190.57, 0.01)));
    }

    @Test
    public void testGetByEmployeeId_MultipleRecords() throws Exception {
        PayrollSummary sample3 = new PayrollSummary();
        sample3.setPayrollSummaryId(3L);
        sample3.setEmployeeId(5L);
        sample3.setEmployeeName("Katam, Manoj");
        sample3.setCheckDate(LocalDate.of(2025, 11, 28));
        sample3.setTotalPaid(4076.81f);
        sample3.setNetPay(3190.57f);

        when(payrollSummaryService.getByEmployeeId(5L)).thenReturn(List.of(sample1, sample3));

        mockMvc.perform(get("/api/v1/payrollsummary/getByEmployeeId/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].checkDate[0]", is(2025)))
                .andExpect(jsonPath("$[0].checkDate[1]", is(12)))
                .andExpect(jsonPath("$[0].checkDate[2]", is(26)))
                .andExpect(jsonPath("$[1].checkDate[0]", is(2025)))
                .andExpect(jsonPath("$[1].checkDate[1]", is(11)))
                .andExpect(jsonPath("$[1].checkDate[2]", is(28)));
    }

    @Test
    public void testGetByEmployeeId_NoRecords() throws Exception {
        when(payrollSummaryService.getByEmployeeId(5L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/payrollsummary/getByEmployeeId/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetByEmployeeId_WrongEmployee_ReturnsEmpty() throws Exception {
        when(payrollSummaryService.getByEmployeeId(999L)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/payrollsummary/getByEmployeeId/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
