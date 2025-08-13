package com.m7md7sn.dentary.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.m7md7sn.dentary.data.model.Screen
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue
import com.m7md7sn.dentary.presentation.ui.addpatient.AddPatientScreen
import com.m7md7sn.dentary.presentation.ui.auth.emailverification.EmailVerificationScreen
import com.m7md7sn.dentary.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentary.presentation.ui.auth.passwordreset.PasswordResetScreen
import com.m7md7sn.dentary.presentation.ui.auth.register.RegisterScreen
import com.m7md7sn.dentary.presentation.ui.home.HomeScreen
import com.m7md7sn.dentary.presentation.ui.medicalhistory.AddMedicalHistoryScreen
import com.m7md7sn.dentary.presentation.ui.splash.SplashScreen
import com.m7md7sn.dentary.presentation.ui.patients.PatientsScreen
import com.m7md7sn.dentary.presentation.ui.profile.ProfileScreen
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen
import com.m7md7sn.dentary.presentation.ui.settings.SettingsViewModel
import com.m7md7sn.dentary.presentation.ui.patient.PatientScreen

@Composable
fun DentaryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    val showBottomBar = when (currentRoute) {
        Screen.Home.route, Screen.Profile.route, Screen.Settings.route, Screen.Appointments.route, Screen.Chats.route -> true
        else -> false
    }
    val showTopBar = when (currentRoute) {
        Screen.Splash.route, Screen.Login.route, Screen.Register.route, Screen.EmailVerification.route, Screen.PasswordReset.route -> false
        else -> true
    }

    val topBarShowBackButton: Boolean = when (currentRoute) {
        Screen.Home.route, Screen.Profile.route, Screen.Appointments.route, Screen.Chats.route -> false
        Screen.Settings.route -> settingsUiState.currentScreen != SettingsScreen.Main
        else -> navController.previousBackStackEntry != null
    }

    val isTopBarColorBlue = currentRoute == Screen.Patient.route || currentRoute == Screen.MedicalHistoryScreen.route

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
                if (currentRoute == Screen.Settings.route) {
                    DentaryTopBar(
                        navController = navController,
                        showBackButton = settingsUiState.currentScreen != SettingsScreen.Main,
                        onBackClick = { settingsViewModel.navigateBack() },
                        onNavDrawerClicked = {},
                        iconColor = if (isTopBarColorBlue) Color.White else DentaryBlue
                    )
                } else {
                    DentaryTopBar(
                        navController = navController,
                        showBackButton = topBarShowBackButton,
                        onBackClick = { navController.popBackStack() },
                        onNavDrawerClicked = {},
                        containerColor = if (isTopBarColorBlue) DentaryLightBlue else BackgroundColor,
                        iconColor = if (isTopBarColorBlue) Color.White else DentaryBlue
                    )
                }
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
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp).fillMaxSize()
                    )
                }
            } else if( currentRoute == Screen.MedicalHistoryScreen.route){
                IconButton(
                    onClick = {

                    },
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color(0xFF5F67EC),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp).fillMaxSize()
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
                    },
                    onNavigateToPatient = { patientId ->
                        navController.navigate(Screen.Patient.createRoute(patientId))
                    }
                )
            }
            composable(route = Screen.Patients.route) {
                PatientsScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToPatient = { patientId ->
                        navController.navigate(Screen.Patient.createRoute(patientId))
                    }
                )
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToPatients = {
                        navController.navigate(Screen.Patients.route)
                    },
                    onNavigateToProfileEdit = {
                        navController.navigate(Screen.Settings.route)
                        settingsViewModel.navigateToScreen(SettingsScreen.EditDoctorAndClinicInfo)
                    }
                )
            }
            composable(route = Screen.Appointments.route) {
                // Appointments screen content
            }
            composable(route = Screen.Chats.route) {
                // Chats screen content
            }
            composable(route = Screen.Settings.route) {
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(route = Screen.AddPatient.route) {
                AddPatientScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = Screen.Patient.route,
                arguments = listOf(navArgument("patientId") { type = NavType.StringType })
            ) { backStackEntry ->
                val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                PatientScreen(patientId = patientId, navController = navController)
            }

            composable(
                route = Screen.MedicalHistoryScreen.route,
                arguments = listOf(navArgument("patientId") { type = NavType.StringType })
            ) { backStackEntry ->
                val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
                AddMedicalHistoryScreen(patientId = patientId)
            }
        }
    }
}