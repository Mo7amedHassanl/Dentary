package com.m7md7sn.dentary.presentation.ui.medicalhistory

import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m7md7sn.dentary.data.model.MedicalHistory
import com.m7md7sn.dentary.data.repository.AttachmentCandidate
import com.m7md7sn.dentary.domain.usecase.medicalhistory.SaveMedicalHistoryUseCase
import com.m7md7sn.dentary.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

data class AddMedicalHistoryUiState(
    val description: String = "",
    val showAttachmentSheet: Boolean = false,
    val isSaving: Boolean = false,
    val attachments: List<AttachmentCandidate> = emptyList(),
    val savedMedicalHistory: MedicalHistory? = null,
    val error: String? = null
)

@HiltViewModel
class AddMedicalHistoryViewModel @Inject constructor(
    private val saveMedicalHistoryUseCase: SaveMedicalHistoryUseCase,
    private val application: Application
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMedicalHistoryUiState())
    val uiState: StateFlow<AddMedicalHistoryUiState> = _uiState.asStateFlow()

    fun onDescriptionChanged(text: String) {
        _uiState.value = _uiState.value.copy(description = text)
    }

    fun showAttachmentSheet() {
        _uiState.value = _uiState.value.copy(showAttachmentSheet = true)
    }

    fun hideAttachmentSheet() {
        _uiState.value = _uiState.value.copy(showAttachmentSheet = false)
    }

    fun commitAttachment(uri: Uri, fileName: String, mimeType: String, description: String) {
        val candidate = AttachmentCandidate(
            uri = uri,
            fileName = fileName,
            description = description,
            mimeType = mimeType
        )
        _uiState.value = _uiState.value.copy(
            attachments = _uiState.value.attachments + candidate
        )
    }

    fun removeAttachment(index: Int) {
        val attachments = _uiState.value.attachments.toMutableList()
        if (index in attachments.indices) {
            attachments.removeAt(index)
            _uiState.value = _uiState.value.copy(attachments = attachments)
        }
    }

    fun save(patientId: String) {
        val state = _uiState.value
        if (state.description.isBlank() && state.attachments.isEmpty()) {
            _uiState.value = state.copy(error = "Please add a description or attachments")
            return
        }

        _uiState.value = state.copy(isSaving = true, error = null)

        viewModelScope.launch {
            val localAttachments = withContext(Dispatchers.IO) {
                state.attachments.map { candidate ->
                    val targetDir = File(application.filesDir, "attachments")
                    targetDir.mkdirs()
                    val targetFile = File(targetDir, "${UUID.randomUUID()}_${candidate.fileName}")
                    application.contentResolver.openInputStream(candidate.uri)?.use { input ->
                        targetFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    candidate.copy(uri = Uri.fromFile(targetFile))
                }
            }

            val result = saveMedicalHistoryUseCase(
                SaveMedicalHistoryUseCase.Params(
                    patientId = patientId,
                    description = state.description,
                    attachments = localAttachments
                )
            )

            when (result) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        savedMedicalHistory = result.data,
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = result.message ?: "Failed to save medical history"
                    )
                }
                is Result.Loading -> {}
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
