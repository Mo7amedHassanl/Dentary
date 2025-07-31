package com.m7md7sn.dentary.presentation.ui.profile.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.AlexandriaBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaMedium
import com.m7md7sn.dentary.presentation.theme.AlexandriaRegular
import com.m7md7sn.dentary.presentation.theme.AlexandriaSemiBold
import com.m7md7sn.dentary.presentation.theme.AlexandriaBlack
import com.m7md7sn.dentary.presentation.theme.DentaryBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkBlue
import com.m7md7sn.dentary.presentation.theme.DentaryDarkGray
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.utils.rememberImagePicker
import com.m7md7sn.dentary.presentation.ui.auth.register.compoenents.SectionTitle
import com.m7md7sn.dentary.presentation.ui.profile.ProfileUiState

@Composable
fun ProfileUserInformation(
    uiState: ProfileUiState,
    onEditProfileClick: () -> Unit,
    onSeeAllPatientsClick: () -> Unit,
    onUpdateProfilePicture: (android.net.Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditProfileButton(onClick = onEditProfileClick)
        ProfilePicture(
            profilePictureUrl = uiState.profile?.profilePicture,
            onUpdateProfilePicture = onUpdateProfilePicture,
            modifier = Modifier
        )
        Spacer(Modifier.height(12.dp))
        DoctorInformation(
            fullName = uiState.profile?.fullName ?: "غير محدد",
            email = uiState.userEmail ?: "غير محدد",
            specialization = uiState.profile?.specialization ?: "غير محدد"
        )
        Spacer(Modifier.height(24.dp))
        ClinicInformation(
            clinicName = uiState.profile?.clinicName ?: "غير محدد",
            phoneNumber = uiState.profile?.phoneNumber ?: "غير محدد",
            clinicAddress = uiState.profile?.clinicAddress ?: "غير محدد"
        )
        Spacer(Modifier.height(10.dp))
        TotalPatientsCard(
            totalPatients = uiState.totalPatients,
            medicalProcedureStats = uiState.medicalProcedureStats,
            onSeeAllClick = onSeeAllPatientsClick
        )
    }
}

@Composable
private fun ClinicInformation(
    clinicName: String,
    phoneNumber: String,
    clinicAddress: String,
) {
    SectionTitle(
        title = R.string.clinic_information,
        titleIcon = R.drawable.ic_clinic,
    )
    Spacer(Modifier.height(16.dp))
    ProfileInfoField(
        text = clinicName,
        icon = R.drawable.ic_clinic_name,
    )
    Spacer(Modifier.height(10.dp))
    ProfileInfoField(
        text = phoneNumber,
        icon = R.drawable.ic_phone,
    )
    Spacer(Modifier.height(10.dp))
    ProfileInfoField(
        text = clinicAddress,
        icon = R.drawable.ic_location,
    )
    Spacer(Modifier.height(10.dp))
    ProfileInfoField(
        text = "شعار العيادة",
        icon = R.drawable.ic_clinic_logo,
        modifier = Modifier.height(66.dp)
    )
}

@Composable
private fun DoctorInformation(
    fullName: String,
    email: String,
    specialization: String,
) {
    Text(
        text = fullName,
        style = TextStyle(
            fontSize = 22.sp,
            fontFamily = AlexandriaBlack,
            fontWeight = FontWeight.Black,
            color = DentaryBlue,
        )
    )
    Spacer(Modifier.height(18.dp))
    Text(
        text = specialization,
        style = TextStyle(
            fontSize = 15.sp,
            fontFamily = AlexandriaSemiBold,
            fontWeight = FontWeight.SemiBold,
            color = DentaryLightBlue,
        )
    )
    Spacer(Modifier.height(18.dp))
    ProfileInfoField(
        text = email,
        icon = R.drawable.ic_email,
    )
}

@Composable
fun ProfileInfoField(
    text: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(Color.White, shape = CircleShape)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = AlexandriaRegular,
                fontWeight = FontWeight.Normal,
                color = DentaryBlue,
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ProfilePicture(
    profilePictureUrl: String?,
    onUpdateProfilePicture: (android.net.Uri) -> Unit,
    modifier: Modifier
) {
    val imagePicker = rememberImagePicker { uri ->
        uri?.let { onUpdateProfilePicture(it) }
    }
    
    Box(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .size(105.dp)
                .background(DentaryBlue, shape = CircleShape)
                .clip(CircleShape)
                .clickable(enabled = true, onClick = { imagePicker() }),
            contentAlignment = Alignment.Center
        ) {
            if (profilePictureUrl != null) {
                // Show profile picture using Coil
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profilePictureUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Show default user icon
            Image(
                painter = painterResource(id = R.drawable.ic_user_sharp),
                contentDescription = null,
                modifier = Modifier.height(46.dp).width(40.dp)
            )
        }
        }
        
        // Show add button only when no profile picture is set
        IconButton(
                onClick = { imagePicker() },
            modifier = Modifier
                .size(25.dp)
                .background(DentaryLightBlue, CircleShape)
                .align(Alignment.BottomEnd)
                .padding(0.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                    contentDescription = "Add Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }
    }
}

@Composable
fun ColumnScope.EditProfileButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(22.dp)
            .background(DentaryLightBlue, RoundedCornerShape(4.dp))
            .align(Alignment.End)
            .padding(2.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            tint = Color.White
        )
    }
}