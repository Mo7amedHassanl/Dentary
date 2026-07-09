package com.m7md7sn.dentary.domain.usecase.medicalhistory

import com.m7md7sn.dentary.data.model.MedicalHistory
import com.m7md7sn.dentary.data.repository.MedicalHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicalHistoriesUseCase @Inject constructor(
    private val repository: MedicalHistoryRepository
) {
    operator fun invoke(patientId: String): Flow<List<MedicalHistory>> {
        return repository.getMedicalHistoriesByPatientFlow(patientId)
    }
}
