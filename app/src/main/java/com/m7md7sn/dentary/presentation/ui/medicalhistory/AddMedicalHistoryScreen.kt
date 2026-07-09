package com.m7md7sn.dentary.presentation.ui.medicalhistory

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.m7md7sn.dentary.R
import com.m7md7sn.dentary.presentation.theme.BackgroundColor
import com.m7md7sn.dentary.presentation.theme.DentaryLightBlue
import com.m7md7sn.dentary.presentation.theme.DentaryTheme
import com.m7md7sn.dentary.presentation.ui.medicalhistory.components.AddAttachmentBottomSheet
import com.m7md7sn.dentary.presentation.ui.medicalhistory.components.AddMedicalHistoryContent
import com.m7md7sn.dentary.presentation.ui.medicalhistory.components.AddMedicalHistoryHeader
import com.m7md7sn.dentary.presentation.ui.patient.PatientViewModel

@Composable
fun AddMedicalHistoryScreen(
    patientId: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    patientViewModel: PatientViewModel = hiltViewModel(),
    medicalHistoryViewModel: AddMedicalHistoryViewModel = hiltViewModel()
) {
    val patientState by patientViewModel.uiState.collectAsState()
    val medicalState by medicalHistoryViewModel.uiState.collectAsState()
    val patient = patientState.patient
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(patientId) {
        patientViewModel.loadPatient(patientId)
    }

    LaunchedEffect(medicalState.savedMedicalHistory) {
        if (medicalState.savedMedicalHistory != null) {
            onNavigateBack()
        }
    }

    LaunchedEffect(medicalState.error) {
        medicalState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            medicalHistoryViewModel.clearError()
        }
    }

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("") }
    var selectedFileMimeType by remember { mutableStateOf("") }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val mimeType = context.contentResolver.getType(it) ?: "application/octet-stream"
            val cursor = context.contentResolver.query(it, null, null, null, null)
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val nameIndex = c.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    val fileName = if (nameIndex >= 0) c.getString(nameIndex) else "Unknown"
                    selectedFileUri = it
                    selectedFileName = fileName
                    selectedFileMimeType = mimeType
                }
            } ?: run {
                val fileName = it.lastPathSegment ?: "Unknown"
                selectedFileUri = it
                selectedFileName = fileName
                selectedFileMimeType = mimeType
            }
        }
    }

    Surface(
        color = DentaryLightBlue,
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(color = DentaryLightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.patient_header_bg),
                        contentDescription = null
                    )
                    AddMedicalHistoryHeader(
                        patientName = patient?.name ?: "Unknown Patient",
                        medicalCondition = patient?.medicalProcedure
                            ?: "No Medical Condition",
                        imageUri = patient?.image,
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 4.dp)
                            .background(
                                color = BackgroundColor,
                                shape = RoundedCornerShape(
                                    topEnd = 35.dp,
                                    topStart = 35.dp
                                )
                            )
                    ) {
                        AddMedicalHistoryContent(
                            description = medicalState.description,
                            onDescriptionChanged = medicalHistoryViewModel::onDescriptionChanged,
                            attachments = medicalState.attachments,
                            onRemoveAttachment = medicalHistoryViewModel::removeAttachment,
                            onAddAttachmentClick = medicalHistoryViewModel::showAttachmentSheet,
                            onSave = { medicalHistoryViewModel.save(patientId) },
                            onCancel = onNavigateBack,
                            isSaving = medicalState.isSaving,
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        )
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    if (medicalState.showAttachmentSheet) {
        AddAttachmentBottomSheet(
            selectedFileName = selectedFileName,
            onDismissRequest = {
                selectedFileUri = null
                selectedFileName = ""
                selectedFileMimeType = ""
                medicalHistoryViewModel.hideAttachmentSheet()
            },
            onUploadClick = {
                filePickerLauncher.launch(arrayOf("*/*"))
            },
            onConfirm = { description ->
                if (selectedFileUri != null) {
                    medicalHistoryViewModel.commitAttachment(
                        uri = selectedFileUri!!,
                        fileName = selectedFileName,
                        mimeType = selectedFileMimeType,
                        description = description
                    )
                }
                selectedFileUri = null
                selectedFileName = ""
                selectedFileMimeType = ""
                medicalHistoryViewModel.hideAttachmentSheet()
            }
        )
    }
}

@Preview
@Composable
private fun AddMedicalHistoryScreenPreviewEn() {
    DentaryTheme {
        AddMedicalHistoryScreen("1", onNavigateBack = {})
    }
}

@Preview(locale = "ar")
@Composable
private fun AddMedicalHistoryScreenPreviewAr() {
    DentaryTheme {
        AddMedicalHistoryScreen("", onNavigateBack = {})
    }
}
