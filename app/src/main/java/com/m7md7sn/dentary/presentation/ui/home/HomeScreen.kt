package com.m7md7sn.dentary.presentation.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.m7md7sn.dentary.presentation.theme.DentaryTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

}

@Preview
@Composable
private fun HomeScreenPreviewEn() {
    DentaryTheme {
        HomeScreen()
    }
}

@Preview(locale = "ar")
@Composable
private fun HomeScreenPreviewAr() {
    DentaryTheme {
        HomeScreen()
    }
}