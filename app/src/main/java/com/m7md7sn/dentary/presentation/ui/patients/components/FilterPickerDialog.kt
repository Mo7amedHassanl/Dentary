package com.m7md7sn.dentary.presentation.ui.patients.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue

@Composable
fun FilterPickerDialog(
    availableFilters: List<String>,
    selectedFilters: Set<String>,
    onToggleFilter: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "تصفية حسب الفئة",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = AlexandriaRegular,
                    fontWeight = FontWeight.Bold,
                )
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableFilters) { filter ->
                    val isSelected = selectedFilters.contains(filter)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleFilter(filter) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onToggleFilter(filter) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = DentaryBlue,
                                uncheckedColor = DentaryBlue.copy(alpha = 0.5f)
                            )
                        )
                        Text(
                            text = filter,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = AlexandriaRegular,
                                fontWeight = FontWeight.Normal,
                                color = DentaryBlue
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DentaryBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.confirm),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = AlexandriaRegular,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        },
        containerColor = BackgroundColor,
        titleContentColor = DentaryDarkBlue,
        textContentColor = DentaryBlue,
        icon = {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = null,
                tint = DentaryDarkBlue
            )
        },
        modifier = modifier
    )
}
