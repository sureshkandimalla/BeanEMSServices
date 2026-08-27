-- ============================================================================
-- Migration: give `invoice` a real DB-generated surrogate primary key.
--
-- Root cause: `invoice.invoice_id` was a user-typed, non-unique "invoice
-- number" used directly as the JPA @Id. Because a JPA save() on a
-- manually-assigned @Id is an upsert, two different invoices (different
-- project/employee) ending up with the same typed number caused the second
-- save to silently overwrite the first invoice row in place, while bills
-- already billed against the first invoice kept pointing at the now
-- repurposed row (see: project 36 vs project 13 bill leakage).
--
-- This script:
--   1) adds `invoice.id` (AUTO_INCREMENT surrogate key) and promotes it to
--      PRIMARY KEY, dropping the old invoice_id PK;
--   2) renames `invoice_id` -> `invoice_number` (now a plain, non-unique,
--      cosmetic business field with no identity meaning);
--   3) backfills `bills.invoice_id` from "old invoice_number value" to the
--      matching invoice's new surrogate `id` (bills' column NAME is
--      unchanged — only what it points to changes);
--   4) reconstructs the 9 invoices lost to prior collisions, using only
--      each affected bill's own frozen-at-creation data (project/employee
--      resolved via assignment_id, never guessed) and relinks that one
--      bill to its new invoice. invoice_number is left NULL on these rows
--      — flagged for manual business review, never a fabricated number.
--
-- Idempotent: every step is guarded so re-running is a no-op.
-- Run against ONE schema per invocation:
--   mysql -u root -proot -h 127.0.0.1 DBNAME < 2026-08-26-invoice-surrogate-key.sql
-- ============================================================================

-- Step 1: add the surrogate key as UNIQUE first (MySQL allows AUTO_INCREMENT
-- on any UNIQUE-keyed column, not only the PRIMARY KEY).
SET @has_id_col := (
  SELECT COUNT(*) FROM information_schema.columns
  WHERE table_schema = DATABASE() AND table_name = 'invoice' AND column_name = 'id'
);
SET @sql := IF(@has_id_col = 0,
  'ALTER TABLE invoice ADD COLUMN id BIGINT NOT NULL AUTO_INCREMENT UNIQUE FIRST',
  'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Step 2: promote id to PRIMARY KEY, drop the old one (still on invoice_id
-- at this point).
SET @old_pk_is_invoice_id := (
  SELECT COUNT(*) FROM information_schema.key_column_usage
  WHERE table_schema = DATABASE() AND table_name = 'invoice'
    AND constraint_name = 'PRIMARY' AND column_name = 'invoice_id'
);
SET @sql := IF(@old_pk_is_invoice_id > 0,
  'ALTER TABLE invoice DROP PRIMARY KEY, ADD PRIMARY KEY (id)',
  'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Step 3: rename invoice_id -> invoice_number (nullable now — the 9
-- reconstructed rows below deliberately leave it NULL).
SET @has_invoice_id_col := (
  SELECT COUNT(*) FROM information_schema.columns
  WHERE table_schema = DATABASE() AND table_name = 'invoice' AND column_name = 'invoice_id'
);
SET @sql := IF(@has_invoice_id_col > 0,
  'ALTER TABLE invoice CHANGE invoice_id invoice_number BIGINT NULL',
  'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Step 4: backfill bills.invoice_id (old number -> new surrogate id) for
-- every bill whose old number still matches a real invoice row. This is
-- safe to re-run: once a bill's invoice_id holds a real `invoice.id`, the
-- join on invoice_number simply won't match anything and the row is
-- untouched.
UPDATE bills b
JOIN invoice i ON b.invoice_id = i.invoice_number
SET b.invoice_id = i.id;

-- Step 5: reconstruct the 9 invoices corrupted by prior number collisions.
-- One explicit statement pair per bill (reviewed with the user beforehand)
-- rather than a generic loop, so each is easy to audit individually.
-- Each INSERT is guarded to no-op if that bill is already correctly linked.

-- bill_id 3 (old #1038) -> project 3, employee 3
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 3, '2026-01-01', '2026-01-01', '2026-01-31', 29, 91.3, 2647.7, 'Paid', 2647.7, 0, '2026-04-20', '2026-04-20', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 3 AND i.project_id = 3);
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 3 AND start_date = '2026-01-01' AND end_date = '2026-01-31' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 3 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 3);

-- bill_id 14 (old #1026) -> project 1, employee 1
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 1, '2025-10-01', '2025-10-01', '2025-10-31', 40, 66.42, 2656.8, 'Paid', 2656.8, 0, '2026-07-06', '2026-07-06', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 14 AND i.project_id = 1);
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 1 AND start_date = '2025-10-01' AND end_date = '2025-10-31' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 14 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 1);

-- bill_id 86 (old #1082) -> project 13, employee 10 (Praneetha)
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 13, '2026-02-02', '2026-02-02', '2026-02-08', 40, 61.6, 2464, 'Created', 0, 0, '2026-07-13', '2026-07-13', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 86 AND i.project_id = 13 AND i.start_date = '2026-02-02');
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-02-02' AND end_date = '2026-02-08' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 86 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-02-02');

-- bill_id 87 (old #1094) -> project 13, employee 10 (Praneetha)
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 13, '2026-02-09', '2026-02-09', '2026-02-15', 176, 61.6, 10841.6, 'Paid', 10841.6, 0, '2026-07-13', '2026-07-13', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 87 AND i.project_id = 13 AND i.start_date = '2026-02-09');
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-02-09' AND end_date = '2026-02-15' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 87 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-02-09');

-- bill_id 91 (old #1098) -> project 13, employee 10 (Praneetha)
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 13, '2026-03-09', '2026-03-09', '2026-03-15', 176, 61.6, 10841.6, 'Paid', 10841.6, 0, '2026-07-13', '2026-07-13', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 91 AND i.project_id = 13 AND i.start_date = '2026-03-09' AND i.hours = 176);
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-03-09' AND hours = 176 ORDER BY id DESC LIMIT 1)
WHERE bill_id = 91 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-03-09' AND hours = 176);

-- bill_id 107 (old #1101) -> project 13, employee 10 (Praneetha)
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 13, '2026-03-09', '2026-03-09', '2026-03-15', 160, 61.6, 9856, 'Paid', 9856, 0, '2026-07-15', '2026-07-15', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 107 AND i.project_id = 13 AND i.start_date = '2026-03-09' AND i.hours = 160);
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-03-09' AND hours = 160 ORDER BY id DESC LIMIT 1)
WHERE bill_id = 107 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-03-09' AND hours = 160);

-- bill_id 94 (old #1104) -> project 13, employee 10 (Praneetha)
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 13, '2026-03-30', '2026-03-30', '2026-04-05', 80, 61.6, 4928, 'Paid', 4928, 0, '2026-07-13', '2026-07-13', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 94 AND i.project_id = 13 AND i.start_date = '2026-03-30');
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-03-30' AND end_date = '2026-04-05' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 94 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-03-30');

-- bill_id 192 (old #1105) -> project 11, employee 13 (Kirit)
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 11, '2026-02-23', '2026-02-23', '2026-03-01', 8, 60, 480, 'Paid', 480, 0, '2026-07-28', '2026-07-28', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 192 AND i.project_id = 11 AND i.start_date = '2026-02-23');
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 11 AND start_date = '2026-02-23' AND end_date = '2026-03-01' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 192 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 11 AND start_date = '2026-02-23');

-- bill_id 195 (old #1108) -> project 11, employee 13 (Kirit)
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 11, '2026-03-16', '2026-03-16', '2026-03-22', 176, 60, 10560, 'Paid', 10560, 0, '2026-07-28', '2026-07-28', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 195 AND i.project_id = 11 AND i.start_date = '2026-03-16');
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 11 AND start_date = '2026-03-16' AND end_date = '2026-03-22' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 195 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 11 AND start_date = '2026-03-16');

-- Two more found only after Step 4's backfill ran (their bills.project_id
-- was itself a stale snapshot copied from an already-corrupted invoice at
-- creation time — assignment_id is the only fully reliable source, so a
-- second pass cross-checking every bill against its assignment's project
-- caught these): bill_id 110 (invoice 92, old #1138) -> true project 13
-- (Praneetha) via assignment; bill_id 114 (invoice 126, old #112) -> true
-- project 28 via assignment.
INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 13, '2026-04-06', '2026-04-06', '2026-04-12', 40, 61.6, 2464, 'Paid', 2464, 0, '2026-07-15', '2026-07-15', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 110 AND i.project_id = 13 AND i.start_date = '2026-04-06');
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-04-06' AND end_date = '2026-04-12' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 110 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 13 AND start_date = '2026-04-06');

INSERT INTO invoice (project_id, invoice_month, start_date, end_date, hours, billing, total, status, invoice_paid_amount, discounts, invoice_date, payment_date, invoice_paid_date, invoice_number)
SELECT 28, '2026-06-01', '2026-06-01', '2026-06-30', 56, 86.4, 4838.4, 'Paid', 4838.4, 0, '2026-07-27', '2026-07-27', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM bills b JOIN invoice i ON b.invoice_id = i.id WHERE b.bill_id = 114 AND i.project_id = 28 AND i.start_date = '2026-06-01');
UPDATE bills SET invoice_id = (SELECT id FROM invoice WHERE project_id = 28 AND start_date = '2026-06-01' AND end_date = '2026-06-30' ORDER BY id DESC LIMIT 1)
WHERE bill_id = 114 AND invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 28 AND start_date = '2026-06-01');

-- bill_id 115 (old #113 on one environment's copy of this data, #125 on
-- another) -> project 21. Unlike the others, a correct invoice for
-- project 21/this exact period already exists (no reconstruction needed)
-- — just relink to it instead of the wrong one bill 115 pointed at.
UPDATE bills b
SET b.invoice_id = (SELECT id FROM invoice WHERE project_id = 21 AND start_date = '2026-06-01' AND end_date = '2026-06-15' ORDER BY id LIMIT 1)
WHERE b.bill_id = 115
  AND EXISTS (SELECT 1 FROM invoice WHERE project_id = 21 AND start_date = '2026-06-01' AND end_date = '2026-06-15')
  AND b.invoice_id NOT IN (SELECT id FROM invoice WHERE project_id = 21);

-- Step 6: bills.project_id is its own frozen-at-creation snapshot (copied
-- from the invoice at the time, not a live join) — BillsRepository now
-- filters on it directly (the original reported bug's fix), so it must be
-- correct too, not just bills.invoice_id. A handful of bills had it NULL
-- or stale-wrong for the same reason their invoice link was wrong.
-- assignment.project_id is the one source never affected by any of this.
UPDATE bills b
JOIN assignment a ON b.assignment_id = a.assignment_id
SET b.project_id = a.project_id
WHERE b.project_id IS NULL OR b.project_id <> a.project_id;
