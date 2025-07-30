package com.m7md7sn.dentary.presentation.ui.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray

val dentalSpecializations = listOf(
    "طب الأسنان العام",
    "تقويم الأسنان",
    "جراحة الفم والوجه والفكين",
    "طب أسنان الأطفال",
    "طب اللثة",
    "تجميل الأسنان",
    "علاج الجذور",
    "طب الأسنان الترميمي",
    "طب الأسنان الوقائي",
    "طب الأسنان التشخيصي"
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSpecializationDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_specialization),
                    contentDescription = null,
                )
            },
            placeholder = {
                Text(
                    text = "التخصص",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = DentaryBlueGray,
                        fontFamily = AlexandriaRegular
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = DentaryBlue,
                focusedTextColor = DentaryBlue,
                unfocusedTextColor = DentaryBlueGray,
                disabledTextColor = DentaryBlueGray,
                disabledContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledBorderColor = DentaryBlueGray,
                focusedBorderColor = DentaryBlue,
                unfocusedBorderColor = DentaryBlueGray,
            ),
            shape = CircleShape,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = DentaryBlue,
                fontFamily = AlexandriaRegular
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(
                width = 1.dp,
                color = DentaryBlue
            ),
        ) {
            dentalSpecializations.forEach { specialization ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = specialization,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = DentaryBlue,
                                fontFamily = AlexandriaRegular
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onValueChange(specialization)
                        expanded = false
                    }
                )
            }
        }
    }
}