package com.m7md7sn.dentary.presentation.ui.patients.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue

@Preview
@Composable
fun PatientList(
    modifier: Modifier = Modifier,
    patients: List<Patient> = emptyList(),
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(patients){
            PatientItem(
                patient = it
            )
        }
    }
}

@Composable
fun PatientItem(
    patient: Patient,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(20.dp),
        onClick = {},
        border = BorderStroke(width = 1.dp, color = Color(0xFFE3E7F2)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth().padding(8.dp),
           horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PatientImage(patient.image)
                PatientInfo(
                    name = patient.name,
                    phoneNumber = patient.phoneNumber ?: "",
                    lastUpdate = patient.lastVisitDate ?: "",
                )
            }
            PatientActionButtons()
        }
    }
}

@Composable
fun PatientActionButtons(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        IconButton(
            onClick = { },
            modifier = Modifier
                .size(40.dp)
                .background(DentaryLightBlue, RoundedCornerShape(10.dp))
                .padding(0.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_call),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp),
                tint = Color.White
            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF5F67EC), RoundedCornerShape(10.dp))
                .padding(0.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chat),
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp),
                tint = Color.White
            )
        }
    }

}

@Composable
fun PatientInfo(
    name: String = "علي حسن علي",
    phoneNumber: String = "0123456789",
    lastUpdate: String = " 2023-10-01",
    modifier: Modifier = Modifier
) {
    val displayName = remember(name) {
        val nameParts = name.trim().split(" ")
        when {
            nameParts.size >= 2 -> "${nameParts[0]} ${nameParts[1]}"
            nameParts.size == 1 -> nameParts[0]
            else -> name // fallback to original name if empty
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = displayName,
            style = TextStyle(
                fontSize = 13.sp,
                fontFamily = AlexandriaBold,
                lineHeight = 30.sp,
                color = Color(0xFF03277A)
            ),
            textAlign = TextAlign.Start,
        )
        Text(
            text = phoneNumber,
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = AlexandriaRegular,
                lineHeight = 30.sp,
                color = Color(0xFF697988)
            ),
            textAlign = TextAlign.Start
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        ) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(DentaryDarkBlue, shape = CircleShape)
            )
            Text(
                text = "${lastUpdate}آخر تحديث: ",
                style = TextStyle(
                    fontSize = 8.sp,
                    fontFamily = AlexandriaRegular,
                    lineHeight = 30.sp,
                    color = Color(0xFF697988)
                ),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
fun PatientImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(60.dp)
            .background(Color(0xFFEEF1FC), shape = RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ){
        if (imageUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_user)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_user),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}