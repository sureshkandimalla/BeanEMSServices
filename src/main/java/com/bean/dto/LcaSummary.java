package com.bean.dto;

import com.bean.model.LCA;

import java.time.LocalDate;

/**
 * Slim LCA representation — omits the redundant Employee back-reference.
 * A Visa linked to this LCA is referenced by lcaId inside VisaSummary.
 * LCAs without a linked Visa are valid (lcaOnly use-case).
 */
public record LcaSummary(
        long lcaId,
        String jobTitle,
        String lcaCaseNumber,
        LocalDate employmentStartDate,
        LocalDate employmentEndDate,
        LocalDate lcaPostedFromDate,
        LocalDate lcaPostedToDate,
        String socCode,
        Long lcaWage,
        String mangerId,
        String jobLocation,
        String jobLocation2,
        String noticePostedLocation,
        String noticePostedLocation2,
        String lcaNumber,
        String client,
        String vendor,
        LocalDate certifiedDate,
        String status,
        LocalDate lastUpdated
) {
    public static LcaSummary from(LCA lca) {
        return new LcaSummary(
                lca.getLcaId(),
                lca.getJobTitle(),
                lca.getLcaCaseNumber(),
                lca.getEmploymentStartDate(),
                lca.getEmploymentEndDate(),
                lca.getLcaPostedFromDate(),
                lca.getLcaPostedToDate(),
                lca.getSocCode(),
                lca.getLcaWage(),
                lca.getMangerId(),
                lca.getJobLocation(),
                lca.getJobLocation2(),
                lca.getNoticePostedLocation(),
                lca.getNoticePostedLocation2(),
                lca.getLcaNumber(),
                lca.getClient(),
                lca.getVendor(),
                lca.getCertifiedDate(),
                lca.getStatus(),
                lca.getLastUpdated()
        );
    }
}
