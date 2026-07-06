# Dentary — Architecture Summary

Generated from static analysis of the repository root (/run/media/m7md7sn/Mix/Android/Dentary). This document summarizes the high-level architecture, module structure, tech stack, data flow, UI conventions, localization, Supabase integration and testing conventions.

---

## 1. Module / Package Structure
Top-level packages (inside app/src/main/java):

- com.m7md7sn.dentary.app.android
  - DentaryApplication (app-level Application subclass)
- com.m7md7sn.dentary.app.host
  - MainActivity (compose host, layout direction management)
- com.m7md7sn.dentary.di
  - SupabaseModule, DatabaseModule, RepositoryModule (Hilt modules)
- com.m7md7sn.dentary.data
  - model — DTOs and serialization (Patient, AuthModels, Appointment, Chat, etc.)
  - repository — repository interfaces & implementations (e.g., PatientRepositoryImpl, AuthRepositoryImpl)
  - source.remote — remote data sources using Supabase/Postgrest (PatientDataSourceImpl, AuthDataSourceImpl, etc.)
  - source.local — Room entities, DAOs, database (PatientEntity, PatientDao, DentaryDatabase)
  - session — session manager for Supabase (SharedPreferencesSessionManager)
  - util — mappers and converters (SupabaseErrorMapper, Converters)
- com.m7md7sn.dentary.domain
  - model — domain error types (DataError)
  - usecase — use case classes (GetPatientsUseCase, LoginUseCase, GetMedicalStatsUseCase)
- com.m7md7sn.dentary.presentation
  - navigation — DentaryNavHost, top/bottom bars, Screen sealed class
  - theme — Material3 theming (Color, Type, Theme)
  - ui — per-screen packages with composables, components and ViewModels (auth, patients, profile, settings, etc.)
  - common — shared composables (CommonButton, CommonTextField)
- com.m7md7sn.dentary.utils
  - LocaleUtils, Result wrapper, ErrorMapper, ImagePicker, etc.

Also: build scripts (build.gradle.kts), project documentation and Supabase setup guides at repository root.

---

## 2. Architecture Pattern & Layer Separation

- Primary pattern: Layered Clean-style architecture with MVVM at presentation layer.
  - Presentation: Jetpack Compose screens + AndroidX ViewModel classes annotated with @HiltViewModel. UI state represented by immutable UiState data classes and exposed as StateFlow in ViewModels.
  - Domain: UseCase layer (classes in domain.usecase) encapsulates business logic and orchestrates repositories. Use cases return Flows where appropriate.
  - Data: Repositories implement application-specific data logic (syncing between local Room database and remote Supabase). Data sources split into remote (Supabase/Postgrest/Storage/Auth) and local (Room DAOs/entities).
  - DI: Dagger Hilt modules bind interfaces to implementations and provide singletons (Supabase client, Room DB, DAOs, repositories).

Separation is enforced by package boundaries and interface abstractions: DataSource interfaces + Repository interfaces with Impl classes bound via Hilt (RepositoryModule).

---

## 3. Tech Stack

- Language & Build
  - Kotlin, Gradle (Kotlin DSL — build.gradle.kts)
  - Android Gradle Plugin, minSdk 24, compileSdk 35
- UI
  - Jetpack Compose (Material3), androidx.navigation.compose
  - Coil (io.coil-kt:coil-compose) for image loading
  - Google Fonts via androidx.ui.text.google.fonts
- Dependency Injection
  - Dagger Hilt (hilt-android, hilt navigation compose)
- Networking / Backend
  - Supabase Kotlin libraries (supabase-kt — auth, postgrest, realtime, storage)
  - Ktor client with OkHttp engine (io.ktor:ktor-client-okhttp)
- Local Storage / DB
  - Room (androidx.room.runtime / room.ktx)
- Serialization & Concurrency
  - kotlinx.serialization for DTOs
  - Kotlin Coroutines & Flow
- Testing
  - JUnit, MockK, kotlinx-coroutines-test

Notable libs: Supabase BOM (io.github.jan-tennert.supabase:bom:3.2.1), Coil, Material3, Hilt, Room.

---

## 4. Compose UI Conventions

- Theming
  - Material3-based theme wrapped in DentaryTheme using dynamic colors on Android 12+.
  - Colors defined in presentation.theme.Color.kt; Typography configured with Google fonts in Type.kt.
  - Theme applied at top-level in MainActivity (DentaryTheme).

- Navigation
  - Navigation implemented with androidx.navigation.compose NavHost + sealed Screen classes (Screen.kt) providing route and helper createRoute() methods for argumentized routes.
  - Nav graph (DentaryNavHost / DentaryApp) sets startDestination to Splash and configures top/bottom bars, FAB behavior and route-specific UI.
  - hiltViewModel() used inside composables to inject ViewModels.

- State Handling / Hoisting
  - ViewModels expose StateFlow<UiState> (MutableStateFlow inside VM, asStateFlow exposed). Composables collectAsState() to render.
  - Each screen has a UiState data class (e.g., PatientsUiState) and ViewModel (PatientsViewModel) that orchestrates usecases.
  - Components are small, stateless composables; they accept state and lambda callbacks (onSearchQueryChange, onRefresh, onPatientClick) — classic state hoisting.
  - Previews include locale variants (e.g., @Preview(locale = "ar")).

- Layout & Edge-to-edge
  - MainActivity.enableEdgeToEdge used with SystemBarStyle adjustments. LocalLayoutDirection provided depending on locale for RTL.

---

## 5. Data Flow (network/DB -> repository -> viewmodel -> UI)

- Remote sources: data.source.remote.* use Supabase Postgrest / Storage / Auth clients to perform network operations (e.g., PatientDataSourceImpl uses postgrest.from("patients").select/insert/update/delete).
- Local sources: Room entities & DAOs represent local persistence (PatientEntity, PatientDao). PatientDao exposes Flow<List<PatientEntity>> for reactive local updates.
- Repositories (e.g., PatientRepositoryImpl):
  - Provide high-level operations to domain layer.
  - Use auth (Auth) to get current userId, call remote data source and persist remote results into Room when successful.
  - Expose getPatientsFlow() which maps DAO Flows to domain models (Patient) — local DB is treated as primary source of truth for UI.
  - GetAllPatients() attempts remote fetch and updates local DB; returns local data if remote fails.
- Use Cases: domain.usecase classes (GetPatientsUseCase) wrap repository operations and typically expose Flows (e.g., emitAll of repository.getPatientsFlow()).
- ViewModels: call use cases, collect Flows in viewModelScope and update _uiState (MutableStateFlow). They expose read-only StateFlow UiState.
- Composables: collect uiState via collectAsState() and render UI; pass event handlers back into ViewModel via hoisted lambdas.

This establishes a reactive flow: Remote -> Repository syncs -> Room -> Flow -> UseCase -> ViewModel StateFlow -> Compose UI.

---

## 6. Naming & File Organization Patterns

- Packages follow layer-first convention: data, domain, presentation, di, utils.
- Interfaces named with generic noun (AuthDataSource, PatientRepository); implementations use Impl suffix (AuthDataSourceImpl, PatientRepositoryImpl).
- ViewModels suffixed with ViewModel; UI state classes suffixed with UiState.
- Composables organized by screen, with components in a `components` subpackage for smaller composables.
- DTOs live in data.model; local DB entities live in data.source.local.entity with toDomain()/toEntity() converters.
- UseCase names `GetPatientsUseCase`, `LoginUseCase` — injected via constructor.

---

## 7. RTL / Localization Handling

- Strings resources provided for default (values/strings.xml) and Arabic (values-ar/strings.xml).
- LocaleUtils persists and applies chosen locale via SharedPreferences and updates app Configuration.
- MainActivity.attachBaseContext uses LocaleUtils.applyLocale to ensure app context uses saved locale.
- MainActivity sets window.decorView.layoutDirection based on Locale (Arabic -> RTL) and uses CompositionLocalProvider(LocalLayoutDirection) to make Compose layout follow direction.
- Previews include locale parameter for Arabic.

---

## 8. Authentication & Supabase Integration

- Supabase client created in di.SupabaseModule via createSupabaseClient() with BuildConfig values injected from local.properties (SUPABASE_URL and SUPABASE_ANON_KEY).
- Ktor HTTP engine configured to use OkHttp; OkHttp client configured to accept all certificates in the module (development mode — hostname verifier accepts all) with long timeouts.
- Supabase installs:
  - Auth (with SharedPreferencesSessionManager for session persistence; autoSaveToStorage & autoLoadFromStorage enabled)
  - Postgrest (database queries)
  - Realtime
  - Storage
- Auth flows implemented in AuthDataSourceImpl (signInWith Email provider, signUpWith Email and OTP verification, password reset, change password via re-auth then updateUser). Errors are mapped to domain DataError via toDataError helpers.
- BuildConfig fields are loaded from `local.properties` at build time (app/build.gradle.kts sets buildConfigField from local.properties keys supabase.url and supabase.anon).

Security note: SupabaseModule currently trusts all SSL certificates (OK for dev only) — a production deployment must remove that bypass.

---

## 9. Testing Conventions

- Unit tests under `app/src/test/java/...` using JUnit4, MockK and kotlinx-coroutines-test.
- Example: GetPatientsUseCaseTest exists and uses mockk to stub repository interactions. (Note: some tests may assume synchronous API rather than Flow; tests can be improved.)
- Android instrumented tests dependencies are declared (androidTest libs) but no extensive androidTest implementations were inspected.

---

## 10. Other Observations & Notes

- Result wrapper: a custom Result sealed-like type (Success / Error / Loading) used across data and presentation layers.
- DataError domain model centralizes error types (e.g., DataError.Auth.SESSION_EXPIRED) and mappers convert Supabase/Ktor exceptions into DataError.
- Image uploads: PatientImageManager and ProfilePictureManager handle storage interactions and integrate with repositories.
- Database migrations: Room version is 1; Converters.kt present for Room type conversions.
- RepositoryModule uses Dagger `@Binds` abstractions for production-friendly DI and easier testing (interfaces -> impls).

---

## Quick File Map (not exhaustive)
- app/src/main/java/com/m7md7sn/dentary/di/SupabaseModule.kt
- app/src/main/java/com/m7md7sn/dentary/di/RepositoryModule.kt
- app/src/main/java/com/m7md7sn/dentary/di/DatabaseModule.kt
- app/src/main/java/com/m7md7sn/dentary/data/source/remote/*.kt
- app/src/main/java/com/m7md7sn/dentary/data/repository/*.kt
- app/src/main/java/com/m7md7sn/dentary/domain/usecase/*.kt
- app/src/main/java/com/m7md7sn/dentary/presentation/ui/* (each screen + components + ViewModel + UiState)
- app/src/main/res/values[-ar]/strings.xml (localization)

---

If a deeper, file-by-file architecture map is needed (call graph, data flow per API endpoint, or an offline-sync strategy review), provide which area to prioritize and I will produce a targeted analysis.

Generated on 2026-07-05T20:46:45.454+03:00
