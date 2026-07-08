package com.m7md7sn.dentary.domain.usecase.patient

import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPatientsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    operator fun invoke(
        query: String = "",
        sortByRecent: Boolean = false
    ): Flow<Result<List<Patient>, DataError>> {
        return patientRepository.getPatientsFlow().map { patients ->
            val filtered = if (query.isBlank()) {
                patients
            } else {
                patients.filter { it.name.contains(query, ignoreCase = true) }
            }

            val sorted = if (sortByRecent) {
                filtered.sortedByDescending { it.createdAt }
            } else {
                filtered.sortedBy { it.name }
            }
            Result.Success(sorted)
        }
    }

    suspend fun refresh(): Result<Unit, DataError> {
        val result = patientRepository.getAllPatients()
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error, result.message)
            is Result.Loading -> Result.Loading
        }
    }

    suspend fun deletePatient(id: String): Result<Unit, DataError> {
        return patientRepository.deletePatient(id)
    }
}