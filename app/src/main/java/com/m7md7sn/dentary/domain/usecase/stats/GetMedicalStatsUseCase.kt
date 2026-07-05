package com.m7md7sn.dentary.domain.usecase.stats

import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.data.repository.PatientRepository
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class GetMedicalStatsUseCase @Inject constructor(
    private val patientRepository: PatientRepository
) {
    suspend operator fun invoke(): Result<List<MedicalProcedureStats>, DataError> {
        val result = patientRepository.getAllPatients()
        
        return when (result) {
            is Result.Success -> {
                val patients = result.data
                
                // Define all medical procedure types
                val allProcedures = listOf(
                    "حشو عادي",
                    "جراحة",
                    "تنظيف جير",
                    "حشو عصب",
                    "تركيبات متحركة",
                    "تركيبات ثابتة"
                )

                // Calculate statistics from patients data
                val patientStats = patients
                    .filter { it.medicalProcedure != null && it.medicalProcedure!!.isNotEmpty() }
                    .groupBy { it.medicalProcedure }
                    .mapValues { it.value.size }

                // Create stats for all procedures, including those with 0 patients
                val stats = allProcedures.map { procedure ->
                    MedicalProcedureStats(
                        procedure = procedure,
                        count = patientStats[procedure] ?: 0
                    )
                }
                
                Result.Success(stats)
            }
            is Result.Error -> result
            is Result.Loading -> Result.Loading
        }
    }
}