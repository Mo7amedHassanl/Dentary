package com.m7md7sn.dentary.presentation.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CloudOff
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.m7md7sn.dentary.data.model.Screen
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue
import com.m7md7sn.dentary.presentation.ui.addpatient.AddPatientScreen
import com.m7md7sn.dentary.presentation.ui.auth.emailverification.EmailVerificationScreen
import com.m7md7sn.dentary.presentation.ui.auth.login.LoginScreen
import com.m7md7sn.dentary.presentation.ui.auth.passwordreset.PasswordResetScreen
import com.m7md7sn.dentary.presentation.ui.auth.register.RegisterScreen
import com.m7md7sn.dentary.presentation.ui.auth.welcome.WelcomeScreen
import com.m7md7sn.dentary.presentation.ui.home.HomeScreen
import com.m7md7sn.dentary.presentation.ui.medicalhistory.AddMedicalHistoryScreen
import com.m7md7sn.dentary.presentation.ui.medicalhistory.AddMedicalHistoryViewModel
import com.m7md7sn.dentary.presentation.ui.splash.SplashScreen
import com.m7md7sn.dentary.presentation.ui.patients.PatientsScreen
import com.m7md7sn.dentary.presentation.ui.patients.PatientsViewModel
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientSortMenu
import com.m7md7sn.dentary.presentation.ui.profile.ProfileScreen
import com.m7md7sn.dentary.presentation.ui.settings.SettingsScreen
import com.m7md7sn.dentary.presentation.ui.settings.SettingsViewModel
import com.m7md7sn.dentary.presentation.ui.patient.PatientScreen
import com.m7md7sn.dentary.presentation.ui.common.ComingSoonPlaceholder
import com.m7md7sn.dentary.utils.ConnectivityObserver

@Composable
fun DentaryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    val networkViewModel: NetworkStatusViewModel = hiltViewModel()
    val networkStatus by networkViewModel.networkStatus.collectAsState()

    val isPatientsScreen = currentRoute?.startsWith("patients") == true
    val patientsViewModel: PatientsViewModel? = if (isPatientsScreen) {
        val entry = remember(navBackStackEntry) {
            try {
                navController.getBackStackEntry(Screen.Patients.route)
            } catch (e: Exception) {
                null
            }
        }
        if (entry != null) hiltViewModel(entry) else null
    } else null
    val patientsUiState by (patientsViewModel?.uiState?.collectAsState() ?: remember { mutableStateOf(null) })

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

    val isMedicalHistoryScreen = currentRoute == Screen.MedicalHistoryScreen.route
    val medicalHistoryViewModel: AddMedicalHistoryViewModel? = if (isMedicalHistoryScreen) {
        val entry = remember(navBackStackEntry) {
            try {
                navController.getBackStackEntry(Screen.MedicalHistoryScreen.route)
            } catch (e: Exception) {
                null
            }
        }
        if (entry != null) hiltViewModel(entry) else null
    } else null

    val isTopBarColorBlue = currentRoute == Screen.Patient.route || isMedicalHistoryScreen

    val showFAB = when (currentRoute) {
        Screen.Home.route, Screen.Patients.route -> true
        else -> false
    }

    val isSelectionMode = patientsUiState?.isSelectionMode == true

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
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
                Box {
                    if (currentRoute == Screen.Settings.route) {
                        DentaryTopBar(
                            navController = navController,
                            showBackButton = settingsUiState.currentScreen != SettingsScreen.Main,
                            onBackClick = { settingsViewModel.navigateBack() },
                            onNavDrawerClicked = {},
                            iconColor = if (isTopBarColorBlue) Color.White else DentaryBlue,
                            actions = {
                                IconButton(
                                    onClick = { /* onNavDrawerClicked() */ },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_nav_drawer),
                                        contentDescription = "Notifications",
                                        tint = if (isTopBarColorBlue) Color.White else DentaryBlue
                                    )
                                }
                            }
                        )
                    } else {
                        DentaryTopBar(
                            navController = navController,
                            showBackButton = topBarShowBackButton,
                            onBackClick = { navController.popBackStack() },
                            onNavDrawerClicked = {},
                            containerColor = if (isTopBarColorBlue) DentaryLightBlue else BackgroundColor,
                            iconColor = if (isTopBarColorBlue) Color.White else DentaryBlue,
                            actions = {
                                if (isPatientsScreen && patientsViewModel != null && patientsUiState != null) {
                                    PatientSortMenu(
                                        currentSortOrder = patientsUiState!!.sortOrder,
                                        onSortOrderChange = { patientsViewModel.setSortOrder(it) },
                                        isSelectionMode = patientsUiState!!.isSelectionMode,
                                        onEnterSelectionMode = { patientsViewModel.enterSelectionMode() },
                                        onDeleteSelected = { patientsViewModel.deleteSelectedPatients() },
                                        onClearSelection = { patientsViewModel.clearSelection() },
                                        iconColor = if (isTopBarColorBlue) Color.White else DentaryBlue
                                    )
                                } else {
                                    IconButton(
                                        onClick = { /* onNavDrawerClicked() */ },
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_nav_drawer),
                                            contentDescription = "Notifications",
                                            tint = if (isTopBarColorBlue) Color.White else DentaryBlue
                                        )
                                    }
                                }
                            }
                        )
                    }

                    if (networkStatus != ConnectivityObserver.Status.Available) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp)
                                .align(Alignment.BottomCenter),
                            color = DentaryBlue.copy(alpha = 0.8f)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.CloudOff,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.offline_mode),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (showFAB) {
                if (isSelectionMode && isPatientsScreen) {
                    IconButton(
                        onClick = {
                            patientsViewModel?.deleteSelectedPatients()
                        },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = stringResource(R.string.delete_selected),
                            modifier = Modifier.padding(8.dp).fillMaxSize()
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.AddPatient.createRoute())
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
                }
            } else if (currentRoute == Screen.MedicalHistoryScreen.route) {
                IconButton(
                    onClick = {
                        medicalHistoryViewModel?.showAttachmentSheet()
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = BackgroundColor
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
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
                            popUpTo(0) { inclusive = true }
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
                            navController.navigate(Screen.Welcome.route) {
                                popUpTo(Screen.EmailVerification.route) { inclusive = true }
                            }
                        },
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(route = Screen.Welcome.route) {
                    WelcomeScreen(
                        onGetStartedClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true }
                            }
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
                        onNavigateToPatients = { query ->
                            navController.navigate(Screen.Patients.createRoute(query))
                        },
                        onNavigateToPatient = { patientId ->
                            navController.navigate(Screen.Patient.createRoute(patientId))
                        }
                    )
                }
                composable(
                    route = Screen.Patients.route,
                    arguments = listOf(navArgument("searchQuery") { 
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    })
                ) { backStackEntry ->
                    val searchQuery = backStackEntry.arguments?.getString("searchQuery")
                    PatientsScreen(
                        initialSearchQuery = searchQuery,
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
                    ComingSoonPlaceholder(
                        iconRes = R.drawable.ic_calendar,
                        descriptionRes = R.string.appointments_placeholder
                    )
                }
                composable(route = Screen.Chats.route) {
                    ComingSoonPlaceholder(
                        iconRes = R.drawable.ic_chat,
                        descriptionRes = R.string.chats_placeholder
                    )
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
                composable(
                    route = Screen.AddPatient.route,
                    arguments = listOf(navArgument("patientId") { 
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    })
                ) {
                    AddPatientScreen(
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToPatient = { patientId ->
                            navController.navigate(Screen.Patient.createRoute(patientId)) {
                                popUpTo(Screen.Patients.route) { inclusive = false }
                            }
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
                    AddMedicalHistoryScreen(
                        patientId = patientId,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}