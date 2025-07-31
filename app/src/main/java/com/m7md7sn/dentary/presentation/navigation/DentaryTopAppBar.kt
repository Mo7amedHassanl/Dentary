package com.m7md7sn.dentary.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DentaryTopBar(
    navController: NavController,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onNavDrawerClicked: () -> Unit = {},
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            if (showBackButton) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Back",
                        tint = DentaryBlue
                    )
                }
            } else if (navController.previousBackStackEntry != null) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Back",
                        tint = DentaryBlue
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = { onNavDrawerClicked() },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nav_drawer),
                    contentDescription = "Notifications",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundColor
        ),
        modifier = Modifier
//            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
    )
}


@Preview(showBackground = true)
@Composable
fun DentelTopBarPreview() {
    DentaryTheme {
        DentaryTopBar(navController = rememberNavController())
    }
}