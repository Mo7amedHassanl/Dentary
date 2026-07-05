package com.m7md7sn.dentary.domain.usecase.patient

import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPatientsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    operator fun invoke(
        query: String = "",
        sortByRecent: Boolean = false
    ): Flow<Result<List<Patient>, DataError>> = flow {
        // Start by emitting from the local database
        emitAll(
            patientRepository.getPatientsFlow().map { patients ->
                val filtered = if (query.isBlank()) {
                    patients
                } else {
                    patients.filter { it.name.contains(query, ignoreCase = true) }
                }

                val sorted = if (sortByRecent) {
                    filtered.sortedByDescending { it.createdAt ?: "0" }
                } else {
                    filtered.sortedBy { it.name }
                }
                
                Result.Success(sorted)
            }
        )
    }

    suspend fun refresh(): Result<Unit, DataError> {
        return when (val result = patientRepository.getAllPatients()) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> result
            is Result.Loading -> Result.Loading
        }
    }
}