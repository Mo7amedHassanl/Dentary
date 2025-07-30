package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray

// List of medical procedure types
val medicalProcedureTypes = listOf(
    "حشو عادي",
    "جراحة",
    "تنظيف جير",
    "حشو عصب",
    "تركيبات متحركة",
    "تركيبات ثابتة"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalProcedureDropdown(
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
            placeholder = {
                Text(
                    text = stringResource(R.string.medical_procedure),
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = DentaryBlueGray,
                        fontFamily = AlexandriaMedium
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
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = DentaryBlue,
                fontFamily = AlexandriaMedium
            ),
            modifier = Modifier
                .height(58.dp)
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
            medicalProcedureTypes.forEach { procedure ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = procedure,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = DentaryBlue,
                                fontFamily = AlexandriaMedium,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        onValueChange(procedure)
                        expanded = false
                    },

                )
            }
        }
    }
} 