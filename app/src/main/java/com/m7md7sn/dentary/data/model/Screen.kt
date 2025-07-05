package com.m7md7sn.dentary.data.model

sealed class Screen (
    val route: String
) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Login : Screen("login")
    object Register : Screen("register")
    object PasswordReset : Screen("password_reset")
    object EmailVerification : Screen("email_verification")
    object Patients : Screen("patients")
    object Profile : Screen("profile")
}