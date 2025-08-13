package com.m7md7sn.dentary.presentation.ui.medicalhistory.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBlack
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaSemiBold
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue

@Preview
@Composable
fun AddMedicalHistoryHeader(
    patientName: String = "محمد حسن علي",
    medicalCondition: String = "حشو عصب",
    imageUri: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 44.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = patientName,
                    style = TextStyle(
                        fontSize = 22.sp,
                        fontFamily = AlexandriaBlack,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                    )
                )
                Text(
                    text = medicalCondition,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = AlexandriaSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF073FB8),
                    )
                )
            }
            Spacer(Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .background(Color(0xFFE5EBFE), shape = CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null && imageUri.isNotBlank()) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_user_sharp),
                        contentDescription = null,
                        tint = Color(0xFF2741B2),
                        modifier = Modifier.size(width = 33.dp, height = 38.dp)
                    )
                }
            }
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(Color.White, shape = CircleShape),
        ) {
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_medical_history_color),
                    contentDescription = stringResource(id = R.string.medical_history),
                )
                Text(
                    text = stringResource(id = R.string.medical_history),
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        color = DentaryLighterBlue,
                    ),
                )
            }
        }
    }
}