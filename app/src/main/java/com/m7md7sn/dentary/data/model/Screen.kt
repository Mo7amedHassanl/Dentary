package com.m7md7sn.dentary.data.model

sealed class Screen (
    val route: String
) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object PasswordReset : Screen("password_reset")
    object EmailVerification : Screen("email_verification/{email}") {
        fun createRoute(email: String) = "email_verification/$email"
    }
    object Patients : Screen("patients")
    object Appointments : Screen("appointments")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
    object Chats : Screen("chats")
    object AddPatient: Screen("add_patient")
    object Patient : Screen("patient/{patientId}") {
        fun createRoute(patientId: String) = "patient/$patientId"
    }
    object MedicalHistoryScreen : Screen("medical_history/{patientId}") {
        fun createRoute(patientId: String) = "medical_history/$patientId"
    }
}