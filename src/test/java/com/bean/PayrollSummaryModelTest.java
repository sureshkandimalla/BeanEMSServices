package com.bean;

import com.bean.model.PayrollSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class PayrollSummaryModelTest {

    private PayrollSummary payrollSummary;

    @BeforeEach
    public void setUp() {
        payrollSummary = new PayrollSummary();
        payrollSummary.setPayrollSummaryId(1L);
        payrollSummary.setPayFrequency("Biweekly");
        payrollSummary.setDepartment("CODE9 LLC");
        payrollSummary.setCheckDate(LocalDate.of(2025, 12, 26));
        payrollSummary.setEmployeeName("Katam, Manoj");
        payrollSummary.setHours(80.0f);
        payrollSummary.setTotalPaid(4076.81f);
        payrollSummary.setTaxWithheld(886.24f);
        payrollSummary.setDeductions(0f);
        payrollSummary.setNetPay(3190.57f);
        payrollSummary.setPaymentDetails("50007");
        payrollSummary.setEmployerLiability(334.72f);
        payrollSummary.setTotalExpenses(4411.53f);
    }

    @Test
    public void testPayrollSummaryId() {
        assertEquals(1L, payrollSummary.getPayrollSummaryId());
    }

    @Test
    public void testPayFrequency() {
        assertEquals("Biweekly", payrollSummary.getPayFrequency());
    }

    @Test
    public void testDepartment() {
        assertEquals("CODE9 LLC", payrollSummary.getDepartment());
    }

    @Test
    public void testCheckDate() {
        assertEquals(LocalDate.of(2025, 12, 26), payrollSummary.getCheckDate());
    }

    @Test
    public void testEmployeeName() {
        assertEquals("Katam, Manoj", payrollSummary.getEmployeeName());
    }

    @Test
    public void testHours() {
        assertEquals(80.0f, payrollSummary.getHours());
    }

    @Test
    public void testTotalPaid() {
        assertEquals(4076.81f, payrollSummary.getTotalPaid(), 0.01f);
    }

    @Test
    public void testTaxWithheld() {
        assertEquals(886.24f, payrollSummary.getTaxWithheld(), 0.01f);
    }

    @Test
    public void testDeductions() {
        assertEquals(0f, payrollSummary.getDeductions());
    }

    @Test
    public void testNetPay() {
        assertEquals(3190.57f, payrollSummary.getNetPay(), 0.01f);
    }

    @Test
    public void testPaymentDetails() {
        assertEquals("50007", payrollSummary.getPaymentDetails());
    }

    @Test
    public void testEmployerLiability() {
        assertEquals(334.72f, payrollSummary.getEmployerLiability(), 0.01f);
    }

    @Test
    public void testTotalExpenses() {
        assertEquals(4411.53f, payrollSummary.getTotalExpenses(), 0.01f);
    }

    @Test
    public void testNoArgsConstructor() {
        PayrollSummary ps = new PayrollSummary();
        assertNotNull(ps);
        assertNull(ps.getPayrollSummaryId());
        assertNull(ps.getEmployeeName());
    }
}
