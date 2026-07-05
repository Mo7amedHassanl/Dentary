# Dentary Evolution Plan: Robustness & Scalability

This plan outlines the steps to transform **Dentary** into a production-ready, highly scalable, and robust medical application. We will move from a direct UI-to-Remote-Data approach to a modern, offline-first architecture.

---

## Phase 1: Architecture Modernization (The "Clean" Foundation)
**Goal:** Decouple business logic from UI and data fetching.

### 1.1 Introduce Domain Layer (Use Cases)
- Create a `domain` package.
- Move business logic out of ViewModels into small, single-purpose classes.
- **Key Use Cases to implement:**
    - `GetPatientsUseCase`: Handles sorting and initial filtering.
    - `GetMedicalStatsUseCase`: Moves the calculation logic out of the DataSource.
    - `AuthUseCases`: Wraps login, register, and session checks.

### 1.2 Enhanced Error Handling & Result API
- Refine the `Result` class to handle specific error types (Network, Auth, Database, Unknown).
- Implement a global `ErrorMapper` to convert exceptions into user-friendly strings.

---

## Phase 2: Offline-First Capability (Reliability)
**Goal:** Ensure the app works seamlessly without an internet connection.

### 2.1 Room Database Integration
- Setup **Room** for local persistence.
- Create entities for `Patient`, `Profile`, and `Appointment`.
- Implement DAOs (Data Access Objects).

### 2.2 Offline Sync Strategy (Single Source of Truth)
- Refactor Repositories to follow the "Local-First" pattern:
    1. UI observes a Flow from the Local Database.
    2. Repository triggers a background fetch from Supabase.
    3. Fetched data is saved to Local Database, which automatically updates the UI.
- Implement a "Sync Manager" to handle pending local changes (e.g., adding a patient while offline).

---

## Phase 3: UI/UX & Design System (Consistency)
**Goal:** Standardize the look and feel and improve maintainability.

### 3.1 Design System Expansion
- Move all hardcoded colors and dimensions to a central `DesignSystem` or enhanced `Theme`.
- Create a library of reusable "Stateless" components:
    - `DentaryButton`, `DentaryCard`, `DentaryEmptyState`, `DentaryErrorView`.
- Implement **Shimmer Loading** effects instead of simple progress bars.

### 3.2 Navigation Refinement
- Typed Navigation: Move away from string-based routes to a more type-safe approach (using Kotlin Serialization with Compose Navigation).

---

## Phase 4: Quality Assurance (Verification)
**Goal:** Prevent regressions and ensure stability.

### 4.1 Unit Testing
- Add tests for `UseCases` (Business Logic).
- Add tests for `ViewModels` using MockK and Coroutine Test Dispatchers.

### 4.2 Integration & UI Testing
- Use **Hilt Testing** to run integration tests.
- Basic **Compose UI tests** for critical flows (Login, Adding a Patient).

---

## Phase 5: Scalability & Performance (Future-Proofing)
**Goal:** Prepare for massive data and more developers.

### 5.1 Modularization
- Split the monolithic `:app` module into:
    - `:core:database`
    - `:core:network`
    - `:core:ui-components`
    - `:feature:patients`
    - `:feature:auth`

### 5.2 Performance Optimization
- Implement **Paging 3** for the patient list to handle hundreds/thousands of records efficiently.
- Optimize image loading and caching using **Coil**.

---

## Next Steps: Picking the First Task
We should proceed sequentially, starting with the foundation.

**Recommended Starting Point:** **Phase 1.1 (Domain Layer / Use Cases)**.
Why? It’s the least invasive change that immediately improves code quality and prepares us for adding the local database in Phase 2.

**Which phase would you like to start with?**
