package com.m7md7sn.dentary.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.m7md7sn.dentary.data.model.BottomNavScreen
import com.m7md7sn.dentary.presentation.theme.DentaryBlue

val bottomNavItems = listOf(
    BottomNavScreen.Home,
    BottomNavScreen.Profile,
    BottomNavScreen.Appointments,
    BottomNavScreen.Chats,
    BottomNavScreen.Settings
)

@Composable
fun DentelBottomBar(
    currentRoute: String?,
    onTabSelected: (BottomNavScreen) -> Unit
) {
    NavigationBar(
        containerColor = Color(0xFFE9EDF8),
        modifier = Modifier.height(80.dp)
    ) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onTabSelected(item) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.contentDescription,
                        tint = if (selected) Color.White else DentaryBlue,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (selected) item.selectedColor else Color.Transparent,
                    selectedIconColor = Color.White,
                    unselectedIconColor = DentaryBlue,
                ),
                alwaysShowLabel = false,
//                modifier = Modifier.padding(horizontal = 16.dp).size(width = 52.dp, height = 34.dp)
            )
        }
    }
}