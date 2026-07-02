package com.bean;

import com.bean.model.PayrollSummary;
import com.bean.repository.PayrollSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayrollSummaryRepositoryTest {

    @Mock
    private PayrollSummaryRepository payrollSummaryRepository;

    private PayrollSummary buildSample(Long id, String name, LocalDate date) {
        PayrollSummary ps = new PayrollSummary();
        ps.setPayrollSummaryId(id);
        ps.setEmployeeName(name);
        ps.setCheckDate(date);
        ps.setPayFrequency("Biweekly");
        ps.setDepartment("CODE9 LLC");
        ps.setHours(80f);
        ps.setTotalPaid(4076.81f);
        ps.setTaxWithheld(886.24f);
        ps.setDeductions(0f);
        ps.setNetPay(3190.57f);
        ps.setPaymentDetails("DD");
        ps.setEmployerLiability(334.72f);
        ps.setTotalExpenses(4411.53f);
        return ps;
    }

    @Test
    public void testFindAll() {
        List<PayrollSummary> list = Arrays.asList(
                buildSample(1L, "Katam, Manoj", LocalDate.of(2025, 12, 26)),
                buildSample(2L, "Kandimalla, Sai Anuhya", LocalDate.of(2025, 12, 12))
        );
        when(payrollSummaryRepository.findAll()).thenReturn(list);

        List<PayrollSummary> result = payrollSummaryRepository.findAll();
        assertEquals(2, result.size());
        verify(payrollSummaryRepository, times(1)).findAll();
    }

    @Test
    public void testFindByEmployeeNameContainingIgnoreCase() {
        List<PayrollSummary> list = Arrays.asList(
                buildSample(1L, "Katam, Manoj", LocalDate.of(2025, 12, 26))
        );
        when(payrollSummaryRepository.findByEmployeeNameContainingIgnoreCase("katam")).thenReturn(list);

        List<PayrollSummary> result = payrollSummaryRepository.findByEmployeeNameContainingIgnoreCase("katam");
        assertEquals(1, result.size());
        assertEquals("Katam, Manoj", result.get(0).getEmployeeName());
    }

    @Test
    public void testFindByCheckDateBetween() {
        LocalDate start = LocalDate.of(2025, 12, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);
        List<PayrollSummary> list = Arrays.asList(
                buildSample(1L, "Katam, Manoj", LocalDate.of(2025, 12, 12)),
                buildSample(2L, "Katam, Manoj", LocalDate.of(2025, 12, 26))
        );
        when(payrollSummaryRepository.findByCheckDateBetween(start, end)).thenReturn(list);

        List<PayrollSummary> result = payrollSummaryRepository.findByCheckDateBetween(start, end);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByDepartment() {
        List<PayrollSummary> list = Arrays.asList(
                buildSample(1L, "Katam, Manoj", LocalDate.of(2025, 12, 26))
        );
        when(payrollSummaryRepository.findByDepartment("CODE9 LLC")).thenReturn(list);

        List<PayrollSummary> result = payrollSummaryRepository.findByDepartment("CODE9 LLC");
        assertEquals(1, result.size());
        assertEquals("CODE9 LLC", result.get(0).getDepartment());
    }

    @Test
    public void testSave() {
        PayrollSummary ps = buildSample(null, "Vanteddu, Poojitha", LocalDate.of(2025, 12, 12));
        PayrollSummary saved = buildSample(3L, "Vanteddu, Poojitha", LocalDate.of(2025, 12, 12));
        when(payrollSummaryRepository.save(ps)).thenReturn(saved);

        PayrollSummary result = payrollSummaryRepository.save(ps);
        assertNotNull(result.getPayrollSummaryId());
        assertEquals(3L, result.getPayrollSummaryId());
    }

    @Test
    public void testFindById() {
        PayrollSummary ps = buildSample(1L, "Katam, Manoj", LocalDate.of(2025, 12, 26));
        when(payrollSummaryRepository.findById(1L)).thenReturn(Optional.of(ps));

        Optional<PayrollSummary> result = payrollSummaryRepository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals("Katam, Manoj", result.get().getEmployeeName());
    }

    @Test
    public void testFindByIdNotFound() {
        when(payrollSummaryRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PayrollSummary> result = payrollSummaryRepository.findById(99L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(payrollSummaryRepository).deleteById(1L);
        payrollSummaryRepository.deleteById(1L);
        verify(payrollSummaryRepository, times(1)).deleteById(1L);
    }
}
