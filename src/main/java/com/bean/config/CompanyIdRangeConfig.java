package com.bean.config;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Reserved employeeId ranges per company. Hardcoded here rather than bound
 * from application.properties: Spring's relaxed Map binding for
 * @ConfigurationProperties is ambiguous for keys containing spaces (company
 * names), which silently resolved to no range at all — new employees fell
 * through to the plain DB auto-increment instead of their company's range.
 */
@Component
public class CompanyIdRangeConfig {

    private static final Map<String, long[]> ID_RANGES = Map.of(
            "Intellan Technologies LLC", new long[]{1, 100},
            "Code9 LLC", new long[]{101, 200},
            "Referral", new long[]{900, 949}
    );

    private static final Map<String, long[]> NEW_HIRE_RANGES = Map.of(
            "Intellan Technologies LLC", new long[]{500, 599},
            "Code9 LLC", new long[]{600, 699},
            "Referral", new long[]{950, 999}
    );

    /**
     * Returns the standard range [start, end] for the given company name.
     * Returns null if company is not configured.
     */
    public long[] getRangeFor(String companyName) {
        return ID_RANGES.get(companyName);
    }

    /**
     * Returns the new-hire range [start, end] for the given company name.
     * Returns null if company is not configured.
     */
    public long[] getNewHireRangeFor(String companyName) {
        return NEW_HIRE_RANGES.get(companyName);
    }
}
