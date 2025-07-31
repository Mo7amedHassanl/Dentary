package com.m7md7sn.dentary.data.model

import androidx.compose.ui.graphics.Color
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue

sealed class BottomNavScreen(
    val route: String,
    val icon: Int,
    val selectedColor: Color,
    val contentDescription: String
) {
    object Home : BottomNavScreen(
        route = Screen.Home.route,
        icon = R.drawable.ic_nav_home,
        selectedColor = DentaryLighterBlue,
        contentDescription = Screen.Home.route
    )
    object Profile : BottomNavScreen(
        route = Screen.Profile.route,
        icon = R.drawable.ic_nav_profile,
        selectedColor = DentaryBlue,
        contentDescription = Screen.Profile.route
    )
    object Appointments : BottomNavScreen(
        route = Screen.Appointments.route,
        icon = R.drawable.ic_nav_calendar,
        selectedColor = DentaryBlue,
        contentDescription = Screen.Appointments.route
    )
    object Chats : BottomNavScreen(
        route = Screen.Chats.route,
        icon = R.drawable.ic_nav_chat,
        selectedColor = DentaryBlue,
        contentDescription = Screen.Chats.route
    )
    object Settings : BottomNavScreen(
        route = Screen.Settings.route,
        icon = R.drawable.ic_nav_settings,
        selectedColor = DentaryBlue,
        contentDescription = Screen.Settings.route
    )
}