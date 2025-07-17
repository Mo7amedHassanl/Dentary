package com.m7md7sn.dentary.presentation.ui.patients.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray
import com.m7md7sn.dentary.presentation.theme.DentaryGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientSearchBar(
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = {},
        active = false,
        onActiveChange = {},
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 28.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.search_patient),
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 30.sp,
                    fontFamily = AlexandriaMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF697988),
                    textAlign = TextAlign.Start,
                )
            )
        },
        trailingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier.size(21.dp)
            )
        },
        shape = CircleShape,
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            dividerColor = Color.Transparent,
            inputFieldColors = SearchBarDefaults.inputFieldColors(
                focusedTextColor = DentaryGray,
                unfocusedTextColor = DentaryGray,
                cursorColor = DentaryBlue,
            )
        ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {}
}
