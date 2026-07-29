package com.bean.service;

import com.bean.model.Assignment;
import com.bean.model.TimeSheet;
import com.bean.repository.AssignmentRepository;
import com.bean.repository.TimeSheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TimeSheetService {

    public static final String DRAFT = "Draft";
    public static final String SUBMITTED = "Submitted";
    public static final String APPROVED = "Approved";
    public static final String REJECTED = "Rejected";

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<TimeSheet> getByAssignmentAndMonth(Long assignmentId, YearMonth month) {
        return timeSheetRepository.findByAssignmentIdAndWorkDateBetweenOrderByWorkDate(
                assignmentId, month.atDay(1), month.atEndOfMonth());
    }

    public List<TimeSheet> getByEmployeeAndMonth(Long employeeId, YearMonth month) {
        return timeSheetRepository.findByEmployeeIdAndWorkDateBetweenOrderByWorkDate(
                employeeId, month.atDay(1), month.atEndOfMonth());
    }

    // Arbitrary date range (not just one month) for an employee — backs the
    // Monthly Timesheets page's per-employee monthly-summary view, which
    // spans a project's whole active period in one fetch.
    public List<TimeSheet> getByEmployeeAndRange(Long employeeId, LocalDate start, LocalDate end) {
        return timeSheetRepository.findByEmployeeIdAndWorkDateBetweenOrderByWorkDate(employeeId, start, end);
    }

    // Every entry for the month across all assignments, for the Monthly
    // Timesheets bulk-entry page — one fetch instead of one per assignment.
    public List<TimeSheet> getByMonth(YearMonth month) {
        return timeSheetRepository.findByWorkDateBetweenOrderByWorkDate(month.atDay(1), month.atEndOfMonth());
    }

    // Every entry across all assignments for an arbitrary date range — backs
    // the Payroll Eligibility page, where a pay period rarely lines up with
    // calendar month boundaries (e.g. biweekly).
    public List<TimeSheet> getByRange(LocalDate start, LocalDate end) {
        return timeSheetRepository.findByWorkDateBetweenOrderByWorkDate(start, end);
    }

    public static class SaveResult {
        public final List<TimeSheet> saved;
        public final List<LocalDate> lockedDates;
        public final List<LocalDate> outOfRangeDates;

        public SaveResult(List<TimeSheet> saved, List<LocalDate> lockedDates, List<LocalDate> outOfRangeDates) {
            this.saved = saved;
            this.lockedDates = lockedDates;
            this.outOfRangeDates = outOfRangeDates;
        }
    }

    /**
     * Upserts each entry by (assignmentId, workDate). Entries with no hours
     * and no existing row are skipped (no point persisting a zero-hour day
     * nobody worked). Rows currently Approved are locked — they're left
     * untouched and their dates are reported back so the caller can tell the
     * user to reopen the month first. Editing a Draft, Submitted, or
     * Rejected row resets it to Draft, since a submission/rejection no
     * longer matches once the hours are changed.
     * Entries outside the assignment's start/end date range are rejected —
     * the UI already greys those days out, but this is enforced server-side
     * too since saveBulk can be called directly.
     */
    public SaveResult saveBulk(List<TimeSheet> entries) {
        List<TimeSheet> saved = new ArrayList<>();
        List<LocalDate> lockedDates = new ArrayList<>();
        List<LocalDate> outOfRangeDates = new ArrayList<>();
        Map<Long, Assignment> assignmentCache = new HashMap<>();

        for (TimeSheet entry : entries) {
            Assignment assignment = assignmentCache.computeIfAbsent(entry.getAssignmentId(),
                    id -> assignmentRepository.findById(id).orElse(null));

            if (assignment != null && !isWithinAssignmentRange(entry.getWorkDate(), assignment)) {
                outOfRangeDates.add(entry.getWorkDate());
                continue;
            }

            Optional<TimeSheet> existing = timeSheetRepository
                    .findByAssignmentIdAndWorkDate(entry.getAssignmentId(), entry.getWorkDate());

            if (existing.isPresent()) {
                TimeSheet ts = existing.get();
                if (APPROVED.equals(ts.getStatus())) {
                    lockedDates.add(entry.getWorkDate());
                    continue;
                }
                ts.setHours(entry.getHours());
                ts.setNotes(entry.getNotes());
                ts.setStatus(DRAFT);
                saved.add(timeSheetRepository.save(ts));
            } else {
                if (entry.getHours() == null || entry.getHours() <= 0) continue;
                entry.setTimesheetId(null);
                entry.setStatus(DRAFT);
                saved.add(timeSheetRepository.save(entry));
            }
        }

        return new SaveResult(saved, lockedDates, outOfRangeDates);
    }

    private boolean isWithinAssignmentRange(LocalDate workDate, Assignment assignment) {
        if (assignment.getStartDate() != null && workDate.isBefore(assignment.getStartDate())) return false;
        if (assignment.getEndDate() != null && workDate.isAfter(assignment.getEndDate())) return false;
        return true;
    }

    public int submitMonth(Long assignmentId, YearMonth month) {
        return submitRange(assignmentId, month.atDay(1), month.atEndOfMonth());
    }

    public int approveMonth(Long assignmentId, YearMonth month) {
        return approveRange(assignmentId, month.atDay(1), month.atEndOfMonth());
    }

    public int reopenMonth(Long assignmentId, YearMonth month) {
        return reopenRange(assignmentId, month.atDay(1), month.atEndOfMonth());
    }

    // Range-scoped versions of the same actions — used by the Monthly
    // Timesheets page's per-week action menu (a week is just a date range
    // that isn't necessarily a whole month).
    public int submitRange(Long assignmentId, LocalDate start, LocalDate end) {
        return updateStatusForRange(assignmentId, start, end, DRAFT, SUBMITTED);
    }

    public int approveRange(Long assignmentId, LocalDate start, LocalDate end) {
        return updateStatusForRange(assignmentId, start, end, SUBMITTED, APPROVED);
    }

    public int rejectRange(Long assignmentId, LocalDate start, LocalDate end) {
        return updateStatusForRange(assignmentId, start, end, SUBMITTED, REJECTED);
    }

    public int reopenRange(Long assignmentId, LocalDate start, LocalDate end) {
        List<TimeSheet> entries = timeSheetRepository
                .findByAssignmentIdAndWorkDateBetweenOrderByWorkDate(assignmentId, start, end);
        int updated = 0;
        for (TimeSheet ts : entries) {
            String status = ts.getStatus();
            if (SUBMITTED.equals(status) || APPROVED.equals(status) || REJECTED.equals(status)) {
                ts.setStatus(DRAFT);
                timeSheetRepository.save(ts);
                updated++;
            }
        }
        return updated;
    }

    private int updateStatusForRange(Long assignmentId, LocalDate start, LocalDate end, String fromStatus, String toStatus) {
        List<TimeSheet> entries = timeSheetRepository
                .findByAssignmentIdAndWorkDateBetweenOrderByWorkDate(assignmentId, start, end);
        int updated = 0;
        for (TimeSheet ts : entries) {
            if (fromStatus.equals(ts.getStatus())) {
                ts.setStatus(toStatus);
                timeSheetRepository.save(ts);
                updated++;
            }
        }
        return updated;
    }
}
