package com.m7md7sn.dentary.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.m7md7sn.dentary.data.model.BottomNavScreen
import com.m7md7sn.dentary.data.model.Screen
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.ui.addpatient.AddPatientScreen
import com.m7md7sn.dentary.presentation.ui.auth.emailverification.EmailVerificationScreen
import com.m7md7sn.dentary.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentary.presentation.ui.auth.passwordreset.PasswordResetScreen
import com.m7md7sn.dentary.presentation.ui.auth.register.RegisterScreen
import com.m7md7sn.dentary.presentation.ui.home.HomeScreen
import com.m7md7sn.dentary.presentation.ui.splash.SplashScreen
import com.m7md7sn.dentary.presentation.ui.patients.PatientsScreen
import com.m7md7sn.dentary.presentation.ui.profile.ProfileScreen
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen

@Composable
fun DentaryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = when (currentRoute) {
        Screen.Home.route, Screen.Profile.route, Screen.Settings.route, Screen.Appointments.route, Screen.Chats.route -> true
        else -> false
    }
    val showTopBar = when (currentRoute) {
        Screen.Splash.route, Screen.Login.route, Screen.Register.route, Screen.EmailVerification.route, Screen.PasswordReset.route -> false
        else -> true
    }

    val topBarShowBackButton: Boolean = when (currentRoute) {
        Screen.Home.route, Screen.Profile.route, Screen.Settings.route, Screen.Appointments.route, Screen.Chats.route -> false
        else -> navController.previousBackStackEntry != null
    }

    val showFAB = when (currentRoute) {
        Screen.Home.route, Screen.Patients.route -> true
        else -> false
    }

    Scaffold(
        modifier = modifier,
        containerColor = BackgroundColor,
        bottomBar = {
            if (showBottomBar) {
                DentelBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { screen ->
                        if (currentRoute != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(Screen.Home.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        },
        topBar = {
            if (showTopBar) {
                DentaryTopBar(
                    navController = navController,
                    showBackButton = topBarShowBackButton,
                    onBackClick = { navController.popBackStack() },
                    onNavDrawerClicked = {}
                )
            }
        },
        floatingActionButton = {
            if (showFAB) {
                IconButton(
                    onClick = {
                        navController.navigate(Screen.AddPatient.route)
                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = DentaryBlue,
                        contentColor = Color.White
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
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
               HomeScreen(
                   onNavigateToPatients = {
                       navController.navigate(Screen.Patients.route)
                   }
               )
            }
            composable(route = Screen.Patients.route) {
                PatientsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen()
            }
            composable(route = Screen.Appointments.route) {
                // Appointments screen content
            }
            composable(route = Screen.Chats.route) {
                // Chats screen content
            }
            composable(route = Screen.Settings.route) {
                SettingsScreen()
            }
            composable(route = Screen.AddPatient.route) {
                AddPatientScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}