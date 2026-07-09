package com.m7md7sn.dentary.domain.usecase.medicalhistory

import com.m7md7sn.dentary.data.model.MedicalHistory
import com.m7md7sn.dentary.data.repository.AttachmentCandidate
import com.m7md7sn.dentary.data.repository.MedicalHistoryRepository
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class SaveMedicalHistoryUseCase @Inject constructor(
    private val repository: MedicalHistoryRepository
) {
    data class Params(
        val patientId: String,
        val description: String,
        val attachments: List<AttachmentCandidate>
    )

    suspend operator fun invoke(params: Params): Result<MedicalHistory, DataError> {
        if (params.description.isBlank() && params.attachments.isEmpty()) {
            return Result.Error(
                DataError.Local.UNKNOWN,
                "Description or attachments must be provided"
            )
        }
        return repository.saveMedicalHistory(
            patientId = params.patientId,
            description = params.description,
            attachments = params.attachments
        )
    }
}
