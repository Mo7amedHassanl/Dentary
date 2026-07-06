# Dentary Project Architecture Summary

This document provides a comprehensive overview of the Dentary mobile application architecture, tech stack, and development conventions.

## 1. Module & Package Structure

The project follows a single-module Android application structure (`:app`) with a clear separation of concerns using a feature-layered package approach.

### Top-Level Packages (com.m7md7sn.dentary)
- **`app`**: Application-level classes (e.g., `DentaryApp` extending `Application`).
- **`di`**: Dependency Injection modules using Hilt.
- **`data`**: Data layer containing repositories, data sources (local and remote), models, and session management.
- **`domain`**: Domain layer containing use cases and domain-specific models (e.g., `DataError`).
- **`presentation`**: UI layer containing Jetpack Compose screens, ViewModels, navigation, and theme.
- **`utils`**: Utility classes and helper functions (e.g., `Result` wrapper).
- **`notification`**: Logic for handling application notifications.

---

## 2. Architecture Pattern

The application implements **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** pattern.

- **Data Layer**: Responsible for data retrieval and persistence. It abstracts whether data comes from Supabase (Remote) or Room (Local).
- **Domain Layer**: Contains the business logic encapsulated in Use Cases. This layer is independent of other layers and UI.
- **Presentation Layer (UI)**: Uses ViewModels to manage state and Jetpack Compose for the UI. It communicates with the Domain layer via Use Cases.
- **State Management**: Uses `StateFlow` and `MutableStateFlow` to manage and expose UI state from ViewModels to Compose.

---

## 3. Tech Stack

- **Build System**: Gradle with Kotlin DSL (`.gradle.kts`).
- **Language**: Kotlin.
- **UI Framework**: Jetpack Compose.
- **Dependency Injection**: Hilt (Dagger).
- **Networking/Backend**: Supabase (Auth, Postgrest, Realtime, Storage).
- **Local Database**: Room (used for caching and offline support).
- **Image Loading**: Coil.
- **Asynchronous Programming**: Kotlin Coroutines and Flow.
- **Serialization**: Kotlinx Serialization.

---

## 4. Compose UI Conventions

- **Theming**: Centralized in `presentation.theme`. Uses a custom `DentaryTheme` wrapper around `MaterialTheme` (Material 3). Colors, Typography, and Shapes are defined here.
- **Navigation**: Uses Jetpack Compose Navigation. The main entry point is `DentaryNavHost` in `AppNavigation.kt`. Screens are defined as routes in a `Screen` sealed class or data class.
- **State Hoisting**: UI state is hoisted to ViewModels. Screens typically take a `ViewModel` (provided via Hilt) and collect its `uiState` flow.
- **Components**: UI is broken down into reusable components (e.g., `HomeHeader`, `PatientItem`) located in `components` sub-packages within each feature UI package.
- **Typos**: Note that some packages currently have minor typos (e.g., `compoenents`).

---

## 5. Data Flow

1. **Remote/Local Source**: `AuthDataSource`, `PatientDataSource`, etc., fetch data from Supabase or Room.
2. **Repository**: `PatientRepositoryImpl` coordinates between data sources, handles caching logic, and maps data to domain models.
3. **Use Case**: `GetPatientsUseCase` executes specific business logic (e.g., sorting, filtering).
4. **ViewModel**: `HomeViewModel` calls the Use Case, catches results using the `Result` wrapper, and updates a `MutableStateFlow<HomeUiState>`.
5. **UI (Compose)**: `HomeScreen` observes the `uiState` and recomposes when the state changes.

---

## 6. Naming Conventions & File Organization

- **ViewModels**: Suffix with `ViewModel` (e.g., `HomeViewModel`).
- **Screens**: Suffix with `Screen` (e.g., `HomeScreen`).
- **State**: Suffix with `UiState` (e.g., `HomeUiState`).
- **Repositories**: Interface named `NameRepository`, implementation named `NameRepositoryImpl`.
- **Data Sources**: Interface named `NameDataSource`, implementation named `NameDataSourceImpl`.
- **Features**: Grouped by feature under `presentation.ui` (e.g., `auth`, `home`, `patient`, `profile`).

---

## 7. RTL & Localization

- **RTL Support**: The app is designed for RTL (Arabic) and LTR (English).
- **Localization**: Uses standard Android `strings.xml`. Resource folders include `values` (default/English) and `values-ar` (Arabic).
- **Layouts**: Jetpack Compose handles RTL automatically based on the device locale and `LocalLayoutDirection`.

---

## 8. Authentication & Supabase Integration

- **Integration**: Supabase is initialized in `SupabaseModule` (DI).
- **Auth Flow**: `AuthRepository` handles login, registration, password reset, and session management via Supabase Auth.
- **Data Access**: `Postgrest` is used for CRUD operations on database tables. `Storage` is used for profile and patient images.
- **Security**: Supabase GoTrue session management is integrated with `SharedPreferencesSessionManager` for persistence.

---

## 9. Testing Conventions

- **Unit Tests**: Located in `app/src/test`.
- **Frameworks**: JUnit 4, MockK for mocking, and Coroutines Test library for suspending functions.
- **Patterns**: Tests follow the Arrange-Act-Assert (AAA) pattern. Use cases are a primary focus for unit testing (e.g., `GetPatientsUseCaseTest`).
