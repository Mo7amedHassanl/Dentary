package com.m7md7sn.dentary.presentation.ui.medicalhistory.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaSemiBold
import com.m7md7sn.dentary.presentation.theme.DentaryLighterBlue
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientActionButtons
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientImage
import com.m7md7sn.dentary.presentation.ui.patients.components.PatientInfo

@Composable
fun MedicalHistoryAttachmentsList(
    attachments: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(attachments){
            MedicalHistoryAttachmentItem()
        }
    }
}

@Composable
fun MedicalHistoryAttachmentItem(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(width = 1.dp, color = Color(0xFFE3E7F2)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = modifier
                    .size(60.dp)
                    .background(Color(0xFFEEF1FC), shape = RoundedCornerShape(15.dp))
                    .clip(RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_attachments),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = "اسم الملحق",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AlexandriaBold,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                )
                Text(
                    text = "تاريخ الملحق",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = AlexandriaSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        color = DentaryLighterBlue,
                    )
                )
            }
        }
    }
}