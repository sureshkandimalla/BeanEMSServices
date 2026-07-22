# Bills & Invoices — Requirements

Primary class: `com.bean.service.InvoiceService`
Controllers: `com.bean.controller.InvoiceController` (`/api/v1/invoice/**`), `com.bean.controller.BillsController` (`/api/v1/bills/**`)
Purpose: how Invoice and Bill records relate, how a Bill's lifecycle status is derived from its Invoice, and how invoice **periods** (one row per week/month/etc. in the Generate Invoice UI) are generated per project.

Frontend counterparts: `src/Invoice/GenerateInvoiceDetails.requirements.md`, `src/Utils/invoiceTerm.requirements.md` (BeanEMS repo).

## 1. Bill creation — one bill per assignment per invoice

- **R1.1** Whenever an Invoice is created or its hours change (`InvoiceService#createInvoiceObject`, called from `POST /invoice/addInvoices`), the system finds every `Assignment` on the invoice's project that was **active during the invoice's month** (`assignment.startDate`/`endDate` bracket the invoice's `YearMonth`) and creates one `Bills` row per assignment via `mapAssignmentsToBills`.
- **R1.2** A bill's `billing` = the assignment's own wage (not the invoice's/project's client-billing rate); `hours` = the invoice's hours; `total` = `hours * billing`. `assignmentId`/`employeeId` come from the assignment, so a bill is always attributable to exactly one employee even when a project has multiple concurrent assignments.
- **R1.3** `billType` = the assignment's `assignmentType` (e.g. `"EMP_PAY"`) — plain, not a concatenated description string (fixed from an earlier version that produced e.g. `"EMP_PAY - null - for Project 2"`).

## 2. Bill status lifecycle

Three states, in order: **Created → Invoice Cleared → Paid**.

- **R2.1 Created**: the status every new bill starts at (R1.1). Represents "billed, but the client hasn't paid the invoice yet."
- **R2.2 Invoice Cleared**: set automatically, for **every** bill billed against a given invoice, the moment that invoice's own `status` is updated to `"Paid"` (`PUT /invoice/invoices/{id}` → `InvoiceService#markBillsInvoiceCleared`). Represents "the client has paid the invoice this bill was part of."
- **R2.3 Paid**: reached independently, when the *bill itself* is paid out (e.g. to the employee/vendor) — handled by `BillsService#updateBill`'s existing paid/unpaid status switch, unrelated to the invoice-driven transition in R2.2. **Never** set or overwritten by the invoice-status flow — R2.2's bulk update explicitly treats a bill already at `Paid` as further along than `Invoice Cleared` and does not downgrade it back.
- **R2.4** These three strings (`"Created"`, `"Invoice Cleared"`, `"Paid"`) replaced an earlier, unrelated use of `bill.status` that mirrored the assignment's own `Active`/`Inactic` status — that was semantically wrong (a bill's status should describe payment progress, not whether the underlying assignment is still active) and has been fully migrated away from (R5).

## 3. Hours sync — editing an invoice's hours propagates to its bills

- **R3.1** Whenever an invoice's `hours` changes — via `PUT /invoice/invoices/{id}` (direct edit) or via `POST /invoice/addInvoices` re-saving an existing invoice (the Generate Invoice grid's Save) — every bill already billed against that `invoiceId` has its `hours` updated to match and `total` recomputed as `hours * bill.billing` (the bill's **own** wage, which may differ from the invoice's rate).
- **R3.2** `InvoiceService#syncBillsForInvoice(invoice, projectId)`: if no bills exist yet for the invoice, creates them (R1); if bills already exist, updates their hours/total in place (R3.1) rather than creating duplicates. This is what fixed a latent bug where re-saving an existing invoice's hours from the Generate Invoice grid used to silently create a *second* set of bills instead of updating the first.
- **R3.3** `applyInvoiceHoursToBills` / `syncBillsHoursForInvoice` are the two public entry points other code should use for this — don't call `createBillsForInvoice` directly for an invoice that might already have bills.

## 4. Invoice period generation per Invoice Term

`ProjectService#createProjectForInvoice(Project, Wage)` — generates the rows the Generate Invoice grid displays (one per invoice period within the project's active date range, `wage.startDate` through `min(wage.endDate, today)`). Branches on `project.getInvoiceTerm()` (string code, see `invoiceTerm.requirements.md` §1 for the code table):

- **R4.1 Weekly (`"1"`)**: one row per calendar week, Monday through Sunday, stepping by 1 week from the Monday on/before `startDate`.
- **R4.2 Biweekly (`"2"`)**: same Monday-anchoring as R4.1, but each row spans 14 days (Monday through the Sunday of the *following* week), stepping by 2 weeks.
- **R4.3 Once in 4 Weeks (`"6"`)**: same Monday-anchoring, 28-day spans, stepping by 4 weeks.
- **R4.4 Semi-Monthly (`"5"`)**: two rows per calendar month — 1st through 15th, and 16th through the last day of the month.
- **R4.5 Monthly (`"3"`), and the fallback for `"4"` (Special) or any unrecognized/null term**: one row per calendar month, 1st through last day — this was the *only* behavior before Weekly/Biweekly/Semi-Monthly/Once-in-4-Weeks were added, and remains the default for anything not explicitly one of the other codes.
- **R4.6** For every term, each generated row is matched against any already-saved Invoice for that exact period:
  - Monthly (R4.5): matched by year-month string equality (`invoice.invoiceMonth` formatted `yyyy-MM` equals the row's month) — a coarser match, kept for backward compatibility with pre-existing monthly invoice data that might not have `startDate` exactly on the 1st.
  - All other terms (R4.1–R4.4): matched by **exact** `periodStart.equals(invoice.getStartDate())` — the newer, stricter match established for Weekly and extended to the rest.
  - When a match is found, the row's `hours`/`invoiceId`/`total` are pre-filled from the matched invoice (so a period that's already been invoiced shows its real numbers, not blank/zero).
- **R4.7 Status per row**: `Active` unless the wage's or project's own `endDate` is before today, in which case `Inactive` — same rule for every term, applied via the shared `buildInvoiceRow` helper.

## 5. Backfill / validation — one-off and rerunnable maintenance endpoints

Needed because R1–R3 were added after real invoice/bill data already existed; these endpoints bring old data in line with the current rules. All are **idempotent** — safe to call repeatedly, a clean run reports zero changes.

- **R5.1 `POST /invoice/backfillBills`**: creates bills for any existing invoice that doesn't have any yet (checked via `BillsRepository#findDistinctInvoiceIdsWithBills`). Returns `{ invoicesBilled: <count> }`.
- **R5.2 `POST /invoice/validateBillStatus`**: walks every invoice, and for each one corrects any of its bills whose `status` doesn't match what the invoice's current status implies (`"Invoice Cleared"` if the invoice is `"Paid"`, `"Created"` otherwise) — this is what migrated the pre-R2 bills (which held stale `Active`/`Inactive` values, R2.4) onto the new three-state lifecycle. Never touches a bill already at `"Paid"` (R2.3). Returns `{ invoicesChecked, billsUpdated }`.
- **R5.3** Run order after any bulk data change (manual DB edit, data import, etc.) that might touch invoices or bills directly: `backfillBills` first, then `validateBillStatus` — bills have to exist before their status can be validated.

## Known gaps

- R4.6's dual matching strategy (year-month string for Monthly, exact date for everything else) is a real inconsistency, kept deliberately rather than "fixed" — changing Monthly to exact-date matching could silently stop matching pre-existing monthly invoices whose `startDate` isn't precisely the 1st. If Monthly invoice data is ever normalized to always have `startDate` on the 1st, this could be unified.
- No automated test suite covers this service (verified manually via direct `curl` round-trips against the live backend for every term and every lifecycle transition, with before/after data captured and restored — see session history). Worth adding integration tests given how many financial-calculation edge cases live here.
- `createProjectForInvoice`'s weekly-family branches (R4.1–R4.3) don't currently re-validate that the Monday-snapped `periodStart` still falls within the project's own `startDate`/`endDate` bounds if snapping pulls it earlier than the wage's actual start — same caveat as the frontend's `invoiceTerm.requirements.md` Known Gaps, on the backend generation side.
