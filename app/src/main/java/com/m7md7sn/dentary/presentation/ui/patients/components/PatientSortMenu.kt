package com.m7md7sn.dentary.presentation.ui.patients.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkGray
import com.m7md7sn.dentary.presentation.theme.DentaryGray
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue
import com.m7md7sn.dentary.presentation.ui.patients.PatientSortOrder

@Composable
fun PatientSortMenu(
    currentSortOrder: PatientSortOrder,
    onSortOrderChange: (PatientSortOrder) -> Unit,
    isSelectionMode: Boolean = false,
    onEnterSelectionMode: () -> Unit = {},
    onDeleteSelected: () -> Unit = {},
    onClearSelection: () -> Unit = {},
    iconColor: Color = DentaryBlue
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_nav_drawer),
                contentDescription = stringResource(R.string.sort_by),
                tint = iconColor
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
        ) {
            if (isSelectionMode) {
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(R.string.delete_selected),
                            color = Color.Red,
                            fontFamily = AlexandriaMedium,
                            fontSize = 14.sp
                        )
                    },
                    onClick = {
                        onDeleteSelected()
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(R.string.clear_selection),
                            fontFamily = AlexandriaMedium,
                            fontSize = 14.sp,
                            color = DentaryBlue
                        )
                    },
                    onClick = {
                        onClearSelection()
                        expanded = false
                    }
                )
            } else {
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(R.string.edit_patients),
                            fontFamily = AlexandriaMedium,
                            fontSize = 14.sp,
                            color = DentaryBlue
                        )
                    },
                    onClick = {
                        onEnterSelectionMode()
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = DentaryBlue
                        )
                    }
                )
            }

            HorizontalDivider(
                color = DentaryLighterBlue.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Text(
                text = stringResource(R.string.sort_by).uppercase(),
                style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = AlexandriaMedium,
                    fontWeight = FontWeight.Medium,
                    color = DentaryGray,
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )

            DropdownMenuItem(
                text = {
                    Text(
                        stringResource(R.string.sort_name),
                        fontFamily = AlexandriaMedium,
                        fontSize = 14.sp,
                        color = if (currentSortOrder == PatientSortOrder.NAME) DentaryBlue else DentaryDarkGray
                    )
                },
                onClick = {
                    onSortOrderChange(PatientSortOrder.NAME)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = null,
                        tint = if (currentSortOrder == PatientSortOrder.NAME) DentaryBlue else DentaryGray
                    )
                },
                trailingIcon = {
                    if (currentSortOrder == PatientSortOrder.NAME) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = DentaryBlue
                        )
                    }
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        stringResource(R.string.sort_last_update),
                        fontFamily = AlexandriaMedium,
                        fontSize = 14.sp,
                        color = if (currentSortOrder == PatientSortOrder.LAST_UPDATE) DentaryBlue else DentaryDarkGray
                    )
                },
                onClick = {
                    onSortOrderChange(PatientSortOrder.LAST_UPDATE)
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = null,
                        tint = if (currentSortOrder == PatientSortOrder.LAST_UPDATE) DentaryBlue else DentaryGray
                    )
                },
                trailingIcon = {
                    if (currentSortOrder == PatientSortOrder.LAST_UPDATE) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = DentaryBlue
                        )
                    }
                }
            )
        }
    }
}
