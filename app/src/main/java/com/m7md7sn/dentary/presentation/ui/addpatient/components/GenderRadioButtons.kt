package com.m7md7sn.dentary.presentation.ui.addpatient.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryBlueGray

enum class Gender(val displayNameRes: Int) {
    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female)
}

@Composable
fun GenderRadioButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 110.dp,
    height: Dp = 41.dp,
    shape: Shape = CircleShape,
    selectedBackgroundColor: Color = DentaryBlue,
    unselectedBackgroundColor: Color = Color.White,
    selectedTextColor: Color = Color.White,
    unselectedTextColor: Color = DentaryBlue,
    borderColor: Color = DentaryBlueGray,
    borderWidth: Dp = 1.dp,
    fontSize: TextUnit = 15.sp,
    fontFamily: FontFamily = AlexandriaBold,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Box(
        modifier = modifier
            .size(width = width, height = height)
            .clip(shape)
            .background(
                if (isSelected) selectedBackgroundColor else unselectedBackgroundColor
            )
            .border(
                width = if (isSelected) 0.dp else borderWidth,
                color = if (isSelected) Color.Transparent else borderColor,
                shape = shape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            color = if (isSelected) selectedTextColor else unselectedTextColor,
            lineHeight = fontSize
        )
    }
}

@Composable
fun GenderRadioButtons(
    modifier: Modifier = Modifier,
    selectedGender: Gender? = null,
    onGenderSelected: (Gender?) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GenderRadioButton(
            text = stringResource(Gender.MALE.displayNameRes),
            isSelected = selectedGender == Gender.MALE,
            onClick = { onGenderSelected(Gender.MALE) }
        )

        GenderRadioButton(
            text = stringResource(Gender.FEMALE.displayNameRes),
            isSelected = selectedGender == Gender.FEMALE,
            onClick = { onGenderSelected(Gender.FEMALE) }
        )
    }
}