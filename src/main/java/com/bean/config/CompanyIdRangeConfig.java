package com.bean.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Binds company.id-ranges.* from application.properties.
 * Each entry maps a company name to a "start:end" range string.
 * Example in application.properties:
 *   company.id-ranges.Intellan Technologies LLC=1:100
 *   company.id-ranges.Code9 LLC=101:200
 *   company.new-hire-ranges.Intellan Technologies LLC=500:599
 *   company.new-hire-ranges.Code9 LLC=600:699
 */
@Component
@ConfigurationProperties(prefix = "company")
public class CompanyIdRangeConfig {

    private Map<String, String> idRanges = new HashMap<>();
    private Map<String, String> newHireRanges = new HashMap<>();

    public Map<String, String> getIdRanges() { return idRanges; }
    public void setIdRanges(Map<String, String> idRanges) { this.idRanges = idRanges; }

    public Map<String, String> getNewHireRanges() { return newHireRanges; }
    public void setNewHireRanges(Map<String, String> newHireRanges) { this.newHireRanges = newHireRanges; }

    /**
     * Returns the standard range [start, end] for the given company name.
     * Returns null if company is not configured.
     */
    public long[] getRangeFor(String companyName) {
        return parseRange(idRanges.get(companyName));
    }

    /**
     * Returns the new-hire range [start, end] for the given company name.
     * Returns null if company is not configured.
     */
    public long[] getNewHireRangeFor(String companyName) {
        return parseRange(newHireRanges.get(companyName));
    }

    private long[] parseRange(String value) {
        if (value == null) return null;
        String[] parts = value.split(":");
        return new long[]{ Long.parseLong(parts[0].trim()), Long.parseLong(parts[1].trim()) };
    }
}
