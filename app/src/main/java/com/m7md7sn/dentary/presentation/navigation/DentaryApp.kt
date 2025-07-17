package com.m7md7sn.dentary.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.m7md7sn.dentary.data.model.Screen
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.ui.auth.emailverification.EmailVerificationScreen
import com.m7md7sn.dentary.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentary.presentation.ui.auth.passwordreset.PasswordResetScreen
import com.m7md7sn.dentary.presentation.ui.auth.register.RegisterScreen
import com.m7md7sn.dentary.presentation.ui.home.HomeScreen
import com.m7md7sn.dentary.presentation.ui.splash.SplashScreen

@Composable
fun DentaryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = BackgroundColor
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Splash.route) {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(route = Screen.Login.route) {
                LoginScreen(
                    onCreateNewAccountClick = {
                        navController.navigate(Screen.Register.route)
                    },
                    onForgetPasswordClick = {
                        navController.navigate(Screen.PasswordReset.route)
                    },
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(route = Screen.Register.route) {
                RegisterScreen(
                    onLoginClick = {
                        navController.navigate(Screen.Login.route)
                    },
                    onNavigateToEmailVerification = { email ->
                        navController.navigate(Screen.EmailVerification.createRoute(email)) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(
                route = Screen.EmailVerification.route,
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) {
                EmailVerificationScreen(
                    onVerificationSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.EmailVerification.route) { inclusive = true }
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screen.PasswordReset.route) {
                PasswordResetScreen(
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.PasswordReset.route) { inclusive = true }
                        }
                    },
                    onPasswordResetSuccess = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.PasswordReset.route) { inclusive = true }
                        }
                    },
                )
            }
            composable(route = Screen.Home.route) {
               HomeScreen()
            }
        }
    }
}