package com.bean;

import com.bean.model.PayrollSummary;
import com.bean.repository.PayrollSummaryRepository;
import com.bean.service.PayrollSummaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayrollSummaryServiceTest {

    @Mock
    private PayrollSummaryRepository payrollSummaryRepository;

    @InjectMocks
    private PayrollSummaryService payrollSummaryService;

    private PayrollSummary sample1;
    private PayrollSummary sample2;

    @BeforeEach
    public void setUp() {
        sample1 = new PayrollSummary();
        sample1.setPayrollSummaryId(1L);
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
    public void testGetAllPayrollSummaries() {
        when(payrollSummaryRepository.findAll()).thenReturn(Arrays.asList(sample1, sample2));

        List<PayrollSummary> result = payrollSummaryService.getAllPayrollSummaries();

        assertEquals(2, result.size());
        verify(payrollSummaryRepository, times(1)).findAll();
    }

    @Test
    public void testGetByEmployeeName() {
        when(payrollSummaryRepository.findByEmployeeNameContainingIgnoreCase("katam"))
                .thenReturn(Arrays.asList(sample1));

        List<PayrollSummary> result = payrollSummaryService.getByEmployeeName("katam");

        assertEquals(1, result.size());
        assertEquals("Katam, Manoj", result.get(0).getEmployeeName());
    }

    @Test
    public void testGetByDateRange() {
        LocalDate start = LocalDate.of(2025, 12, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);
        when(payrollSummaryRepository.findByCheckDateBetween(start, end))
                .thenReturn(Arrays.asList(sample1, sample2));

        List<PayrollSummary> result = payrollSummaryService.getByDateRange(start, end);

        assertEquals(2, result.size());
        verify(payrollSummaryRepository, times(1)).findByCheckDateBetween(start, end);
    }

    @Test
    public void testImportFromCsv_ValidData() throws Exception {
        String csvContent = "Pay Frequency,Department,Check Date,Name,Hours,Total Paid,Tax Withheld,Deductions,Net Pay,Payment Details/Check No,Employer Liability,Total Expenses\n"
                + "Biweekly,CODE9 LLC,12/26/2025 ,\"Katam, Manoj\",80.00,\" $4,076.81 \", $886.24 , $-   ,\" $3,190.57 \",50007, $334.72 ,\" $4,411.53 \"\n"
                + "Biweekly,CODE9 LLC,12/12/2025 ,\"Kandimalla, Sai Anuhya\",80.00,\" $2,000.00 \", $161.60 , $-   ,\" $1,838.40 \",DD, $-   ,\" $2,000.00 \"";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.csv", "text/csv", csvContent.getBytes());

        when(payrollSummaryRepository.saveAll(anyList())).thenReturn(Arrays.asList(sample1, sample2));

        List<PayrollSummary> result = payrollSummaryService.importFromCsv(mockFile);

        assertEquals(2, result.size());
        verify(payrollSummaryRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testImportFromCsv_EmptyFile() throws Exception {
        String csvContent = "Pay Frequency,Department,Check Date,Name,Hours,Total Paid,Tax Withheld,Deductions,Net Pay,Payment Details/Check No,Employer Liability,Total Expenses\n";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "empty.csv", "text/csv", csvContent.getBytes());

        when(payrollSummaryRepository.saveAll(anyList())).thenReturn(List.of());

        List<PayrollSummary> result = payrollSummaryService.importFromCsv(mockFile);

        assertEquals(0, result.size());
    }

    @Test
    public void testImportFromCsv_SkipsInvalidRows() throws Exception {
        String csvContent = "Pay Frequency,Department,Check Date,Name,Hours,Total Paid,Tax Withheld,Deductions,Net Pay,Payment Details/Check No,Employer Liability,Total Expenses\n"
                + "Biweekly,CODE9 LLC,INVALID_DATE,\"Katam, Manoj\",80.00,\" $4,076.81 \", $886.24 , $-   ,\" $3,190.57 \",50007, $334.72 ,\" $4,411.53 \"\n"
                + "Biweekly,CODE9 LLC,12/12/2025 ,\"Kandimalla, Sai Anuhya\",80.00,\" $2,000.00 \", $161.60 , $-   ,\" $1,838.40 \",DD, $-   ,\" $2,000.00 \"";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.csv", "text/csv", csvContent.getBytes());

        when(payrollSummaryRepository.saveAll(anyList())).thenReturn(Arrays.asList(sample2));

        List<PayrollSummary> result = payrollSummaryService.importFromCsv(mockFile);

        // Only 1 valid row saved (invalid date row skipped)
        assertEquals(1, result.size());
    }

    @Test
    public void testImportFromCsv_SkipsRowsWithTooFewColumns() throws Exception {
        String csvContent = "Pay Frequency,Department,Check Date,Name,Hours,Total Paid,Tax Withheld,Deductions,Net Pay,Payment Details/Check No,Employer Liability,Total Expenses\n"
                + "Biweekly,CODE9 LLC,12/26/2025\n"
                + "Biweekly,CODE9 LLC,12/12/2025 ,\"Kandimalla, Sai Anuhya\",80.00,\" $2,000.00 \", $161.60 , $-   ,\" $1,838.40 \",DD, $-   ,\" $2,000.00 \"";

        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.csv", "text/csv", csvContent.getBytes());

        when(payrollSummaryRepository.saveAll(anyList())).thenReturn(Arrays.asList(sample2));

        List<PayrollSummary> result = payrollSummaryService.importFromCsv(mockFile);

        assertEquals(1, result.size());
    }
}
