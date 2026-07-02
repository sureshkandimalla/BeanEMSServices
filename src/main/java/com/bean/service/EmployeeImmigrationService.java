package com.bean.service;

import com.bean.dto.EmployeeImmigrationProfile;
import com.bean.dto.LcaSummary;
import com.bean.dto.VisaSummary;
import com.bean.model.*;
import com.bean.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeImmigrationService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeImmigrationService.class);

    private final EmployeeRepository employeeRepository;
    private final VisaRepository visaRepository;
    private final LCARepository lcaRepository;
    private final ImmigrationDetailsRepository immigrationDetailsRepository;
    private final PassportRepository passportRepository;
    private final NotesRepository notesRepository;

    public EmployeeImmigrationService(
            EmployeeRepository employeeRepository,
            VisaRepository visaRepository,
            LCARepository lcaRepository,
            ImmigrationDetailsRepository immigrationDetailsRepository,
            PassportRepository passportRepository,
            NotesRepository notesRepository) {
        this.employeeRepository = employeeRepository;
        this.visaRepository = visaRepository;
        this.lcaRepository = lcaRepository;
        this.immigrationDetailsRepository = immigrationDetailsRepository;
        this.passportRepository = passportRepository;
        this.notesRepository = notesRepository;
    }

    /**
     * Returns an immigration profile for every employee in the system.
     */
    public List<EmployeeImmigrationProfile> getAllEmployeesImmigration() {
        List<Employee> employees = employeeRepository.findAllSorted();
        return employees.stream()
                .map(this::buildProfile)
                .toList();
    }

    /**
     * Returns an immigration profile for a single employee by ID.
     */
    public Optional<EmployeeImmigrationProfile> getEmployeeImmigration(long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(this::buildProfile);
    }

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private EmployeeImmigrationProfile buildProfile(Employee emp) {
        long id = emp.getEmployeeId();
        logger.debug("Building immigration profile for employeeId={}", id);

        // All Visa records — each embeds its full LCA when linked
        List<VisaSummary> visas = safeList(visaRepository.findByEmployee_EmployeeId(id))
                .stream().map(VisaSummary::from).toList();

        // Collect LCA IDs that are already embedded inside a visa
        java.util.Set<Long> lcaIdsInVisas = visas.stream()
                .filter(v -> v.lca() != null)
                .map(v -> v.lca().lcaId())
                .collect(java.util.stream.Collectors.toSet());

        // LCA-only records — LCAs that have no linked Visa
        List<LcaSummary> lcaList = safeList(lcaRepository.findByEmployee_EmployeeId(id))
                .stream()
                .filter(lca -> !lcaIdsInVisas.contains(lca.getLcaId()))
                .map(LcaSummary::from)
                .toList();

        List<ImmigrationDetails> immigrationTypes = resolveImmigrationDetails(emp, visas);

        // Primary address — prefer the first Address from the employee's address list
        String addressLine = null, city = null, state = null, zipCode = null, country = null;
        List<Address> addresses = emp.getAddress();
        if (addresses != null && !addresses.isEmpty()) {
            Address primary = addresses.get(0);
            addressLine = primary.getAddress();
            city        = primary.getCity();
            state       = primary.getState();
            zipCode     = primary.getZipCode();
            country     = primary.getCountry();
        }

        // Most recent passport number — pick the first passport record for this employee
        String passportNumber = safeList(passportRepository.findByEmployee_EmployeeId(id))
                .stream()
                .map(p -> p.getPassportNumber())
                .filter(pn -> pn != null && !pn.isBlank())
                .findFirst()
                .orElse(null);

        // Notes for this employee
        List<Notes> notes = safeList(notesRepository.findByEmployee_EmployeeId(id));

        return new EmployeeImmigrationProfile(
                emp.getEmployeeId(),
                emp.getFirstName(),
                emp.getLastName(),
                emp.getEmailId(),
                emp.getPhone(),
                emp.getDob(),
                emp.getGender(),
                emp.getDesignation(),
                emp.getEmploymentType(),
                emp.getEmployeeType(),
                emp.getStatus(),
                emp.getCompanyName(),
                emp.getLocation(),
                emp.getVisa(),
                emp.getEverifyStatus(),
                emp.getI9(),
                emp.getPAF(),
                emp.getStartDate(),
                emp.getEndDate(),
                addressLine,
                city,
                state,
                zipCode,
                country,
                immigrationTypes,
                lcaList,
                visas,
                passportNumber,
                notes
        );
    }

    /**
     * Resolve ImmigrationType records for an employee.
     * Strategy:
     *   1. Use Employee.visa (e.g. "H1B") to look up matching rows in ImmigrationType.
     *   2. If none found, try each Visa.visaCategory.
     *   3. If still none, return empty list.
     */
    private List<ImmigrationDetails> resolveImmigrationDetails(Employee emp, List<VisaSummary> visas) {
        // 1. Employee-level visa field
        String empVisaType = emp.getVisa();
        if (empVisaType != null && !empVisaType.isBlank()) {
            List<ImmigrationDetails> found = immigrationDetailsRepository.findByVisaType(empVisaType.trim());
            if (!found.isEmpty()) {
                return found;
            }
        }

        // 2. Fallback: match on each VisaSummary's visaCategory
        for (VisaSummary v : visas) {
            String cat = v.visaCategory();
            if (cat != null && !cat.isBlank()) {
                List<ImmigrationDetails> found = immigrationDetailsRepository.findByVisaType(cat.trim());
                if (!found.isEmpty()) {
                    return found;
                }
            }
        }

        return Collections.emptyList();
    }

    private <T> List<T> safeList(List<T> list) {
        return list != null ? list : Collections.emptyList();
    }
}
