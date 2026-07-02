package com.bean.dto;

import com.bean.model.FilingType;
import com.bean.model.Visa;
import com.bean.model.VisaSubCategory;

import java.time.LocalDate;

/**
 * Slim Visa representation — omits the redundant Employee object.
 * When this Visa is linked to an LCA, the full LCA details are embedded
 * in the {@code lca} field. When there is no LCA, {@code lca} is null.
 */
public record VisaSummary(
        long visaId,
        String visaCategory,
        String receiptNumber,
        LocalDate startDate,
        LocalDate endDate,
        String jobTitle,
        String lcaNumber,
        String socCode,
        String client,
        String vendor,
        String jobLocation,
        String jobLocation2,
        Double lcaWage,
        String status,
        FilingType filingType,
        VisaSubCategory visaSubCategory,
        Integer filingYear,
        LcaSummary lca,          // full LCA details when linked; null otherwise
        LocalDate lastUpdated
) {
    public static VisaSummary from(Visa visa) {
        LcaSummary lcaSummary = (visa.getLca() != null) ? LcaSummary.from(visa.getLca()) : null;
        return new VisaSummary(
                visa.getVisaId(),
                visa.getVisaCategory(),
                visa.getReceiptNumber(),
                visa.getStartDate(),
                visa.getEndDate(),
                visa.getJobTitle(),
                visa.getLcaNumber(),
                visa.getSocCode(),
                visa.getClient(),
                visa.getVendor(),
                visa.getJobLocation(),
                visa.getJobLocation2(),
                visa.getLcaWage(),
                visa.getStatus(),
                visa.getFilingType(),
                visa.getVisaSubCategory(),
                visa.getFilingYear(),
                lcaSummary,
                visa.getLastUpdated()
        );
    }
}
