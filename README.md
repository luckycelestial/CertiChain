# 16. Blockchain-Backed Certificate Issuance & Verification Platform

## 1. Overview
A platform for issuing tamper-proof academic credentials, calculating cryptographic SHA-256 hashes, performing instant verification lookups, and managing institutional certification revenue and verification service budgets.

---

## 2. Primary Actors
- **Admin (University Registrar)**: Registers accredited institutions, manages issuance rules, and audits platform finances.
- **Invoicing User (Platform Accountant)**: Manages server infrastructure POs, bills background-check agencies for API verification lookups, and processes payments.
- **Verifier (Employer / Agency)**: Requests credential verification lookups and pays API query fees.
- **System**: Computes non-null SHA-256 certificate hashes, validates hash lookup matches, and logs API revenue double-entry ledgers.

---

## 3. Master Data Modules
1. **Contact Master**: University Client, Background Verification Agency (Customer), Cloud Hosting Vendor (Vendor).
2. **Product Master**: Certificate Cryptographic Issuance (Service), Verification API Query Package (Service).
3. **Chart of Accounts Master**: Assets (Cryptographic IP Infrastructure, Cash), Liabilities (Cloud Vendor Payables), Income (Verification API Service Revenue), Expenses (Cloud Infrastructure Expenses).
4. **Journal**: Sales Journal (API Queries), Purchase Journal (Server Hosting), Bank/Cash Journals.
5. **Journal Entries**: Ledger records balancing verification API sales against hosting overheads.

---

## 4. Transaction Flow
- **Purchase Order**: Create PO for cloud server hosting and cryptographic security modules.
- **Vendor Bill**: Convert server PO to Vendor Bill and pay via Bank.
- **Sales Order**: Issue order for verification API subscription package to background check firm.
- **Customer Invoice**: Convert API subscription order to Invoice.
- **Payment**: Collect payment via Bank transfer.

---

## 5. Budget Flow
- **Analytic Account**: Certification Operations Unit (e.g., Enterprise Verification API Sector).
- **Budget**: Planned API operational budget vs. actual infrastructure costs and verification revenues.

---

## 6. Reporting Requirements
1. **Balance Sheet**: Platform technology assets and cash balances vs. open cloud vendor liabilities.
2. **Profit & Loss Account**: Verification API sales revenue minus hosting and administrative expenses.
3. **Budget Report**: Verification unit budget performance report.

---

## 7. Key Use-Case Steps
1. **Create Master Data**: Register institution via `POST /api/institutions`, set up verification clients, set up CoA.
2. **Issue & Verify Credential**: Issue credential with SHA-256 hash via `POST /api/certificates`; verify authenticity via `GET /api/certificates/verify/{hash}`.
3. **Process API Billing**: Invoice agency for API query package and register payment via Bank.
4. **Generate Reports**: Run Platform P&L and Unit Budget Reports.
