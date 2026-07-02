package com.bean.dto;

import com.bean.model.ImmigrationDetails;
import com.bean.model.Notes;

import java.time.LocalDate;
import java.util.List;

/**
 * Aggregated immigration profile for a single employee.
 * LCA and Visa are represented as slim summaries to avoid repeating
 * the full Employee object inside each nested entity.
 */
public record EmployeeImmigrationProfile(
        long employeeId,
        String firstName,
        String lastName,
        String emailId,
        String phone,
        String dob,
        String gender,
        String designation,
        String employmentType,
        String employeeType,
        String status,
        String companyName,
        String location,

        // Immigration status fields
        String visaCategory,
        String everifyStatus,
        String i9,
        String paf,
        LocalDate startDate,
        LocalDate endDate,

        // Primary address
        String addressLine,
        String city,
        String state,
        String zipCode,
        String country,

        // ImmigrationType records matching employee's visa category
        List<ImmigrationDetails> immigrationTypes,

        // LCA records that have NO linked Visa (LCA-only entries)
        List<LcaSummary> lcaList,

        // Visa records — each visa embeds its full LCA details when a relation exists;
        // visa.lca is null when there is no associated LCA
        List<VisaSummary> visas,

        // Most recent passport number on file
        String passportNumber,

        // Notes for this employee
        List<Notes> notes
) {}
