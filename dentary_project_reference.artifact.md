# Dentary Project Reference

This document provides a comprehensive overview of the **Dentary** Android application, its architecture, tech stack, and core functionalities.

## 1. Project Overview
**Dentary** is a dental clinic management application designed to help dentists manage their patients, appointments, medical histories, and clinic information efficiently.

## 2. Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Clean Architecture + MVVM
- **Dependency Injection**: Hilt
- **Backend**: Supabase (Auth, Postgrest/Database, Realtime, Storage)
- **Networking**: Ktor (used by Supabase client)
- **Image Loading**: Coil
- **Serialization**: Kotlin Serialization
- **Localization**: Custom locale management (Supports Arabic RTL)

## 3. Architecture Overview
The project follows Clean Architecture principles, divided into layers:

- **`app`**: Application configuration and host Activity.
- **`data`**:
    - **`model`**: Data classes and DTOs.
    - **`repository`**: Interfaces and implementations for data operations.
    - **`source`**: Data sources (Remote via Supabase).
    - **`session`**: Management of user sessions (SharedPreferences).
- **`di`**: Hilt modules for providing dependencies.
- **`presentation`**:
    - **`ui`**: UI screens, ViewModels, and UI state.
    - **`navigation`**: Navigation graph and related components.
    - **`theme`**: Compose themes, colors, and typography.
    - **`common`**: Reusable UI components.
- **`utils`**: Helper classes and utility functions.

## 4. Key Features & Screens

### 4.1. Authentication (`presentation/ui/auth`)
- **Login**: Email/Password authentication.
- **Register**: User account creation.
- **Email Verification**: Post-registration verification.
- **Password Reset**: Forgot password flow.

### 4.2. Home (`presentation/ui/home`)
- Overview of recent patients.
- Quick access to key actions.
- Displays appointments and statistics.

### 4.3. Patient Management (`presentation/ui/patients`, `presentation/ui/patient`, `presentation/ui/addpatient`)
- **Patient List**: Searchable and filterable list of patients.
- **Patient Details**: Comprehensive view of patient information, including medical history.
- **Add Patient**: Form to create new patient records with image support.

### 4.4. Medical History (`presentation/ui/medicalhistory`)
- Tracking patient medical procedures and history.
- Support for attachments.

### 4.5. Profile & Settings (`presentation/ui/profile`, `presentation/ui/settings`)
- **Profile**: Displays doctor/clinic information.
- **Settings**: Manage account info, clinic details, app preferences (language), and notifications.

## 5. Data Layer Details

### 5.1. Models (`data/model`)
- `Patient`: Main patient entity.
- `Profile`: Doctor/Clinic profile entity.
- `Appointment`: Appointment details.
- `AuthModels`: Request/Response models for authentication.

### 5.2. Repositories (`data/repository`)
- `AuthRepository`: Handles auth logic via `AuthDataSource`.
- `PatientRepository`: Manages patient CRUD operations.
- `ProfileRepository`: Manages clinic/doctor profile.
- `ChatRepository` & `AppointmentRepository`: (In progress/Stubs).

## 6. Networking & Backend (Supabase)
The app uses Supabase for almost all backend needs. Configuration is found in `SupabaseModule.kt`.
- **Database**: PostgreSQL via Postgrest.
- **Authentication**: Supabase Auth with session persistence in `SharedPreferencesSessionManager`.
- **Storage**: Used for storing patient and profile images.

## 7. Navigation
- Uses `androidx.navigation.compose`.
- Main navigation logic is in `DentaryNavHost` (in `DentaryApp.kt`).
- Routes are defined in the `Screen` sealed class (`data/model/Screen.kt`).

## 8. Directory Structure (Key Paths)
- `app/src/main/java/com/m7md7sn/dentary/`
    - `app/`: Application and MainActivity.
    - `data/`: Data layer components.
    - `di/`: Dependency injection modules.
    - `presentation/`: UI components (Screens, ViewModels, Navigation).
    - `utils/`: Common utilities.
