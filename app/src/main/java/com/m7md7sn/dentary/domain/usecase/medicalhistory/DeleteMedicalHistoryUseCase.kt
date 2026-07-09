package com.m7md7sn.dentary.domain.usecase.medicalhistory

import com.m7md7sn.dentary.data.repository.MedicalHistoryRepository
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class DeleteMedicalHistoryUseCase @Inject constructor(
    private val repository: MedicalHistoryRepository
) {
    suspend operator fun invoke(id: String): Result<Unit, DataError> {
        return repository.deleteMedicalHistory(id)
    }
}
