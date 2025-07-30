package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.model.CreatePatientRequest
import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.data.repository.PatientImageManager
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject
import android.net.Uri

class PatientDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val patientImageManager: PatientImageManager
) : PatientDataSource {

    override suspend fun getAllPatients(): Result<List<Patient>> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val patients = postgrest
                .from("patients")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<Patient>()

            Result.Success(patients)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch patients")
        }
    }

    override suspend fun getPatientById(id: String): Result<Patient> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val patient = postgrest
                .from("patients")
                .select {
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeSingle<Patient>()

            Result.Success(patient)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch patient")
        }
    }

    override suspend fun createPatient(patient: Patient): Result<Patient> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            // Create PatientInsert object without ID for insertion
            val createPatientRequest = CreatePatientRequest(
                userId = currentUserId,
                name = patient.name,
                phoneNumber = patient.phoneNumber,
                email = patient.email,
                age = patient.age,
                address = patient.address,
                medicalHistory = patient.medicalHistory,
                medicalProcedure = patient.medicalProcedure,
                image = patient.image
            )

            val createdPatient = postgrest
                .from("patients")
                .insert(createPatientRequest) {
                    select()
                }
                .decodeSingle<Patient>()

            Result.Success(createdPatient)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to create patient")
        }
    }

    override suspend fun updatePatient(id: String, patient: Patient): Result<Patient> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val updatedPatient = postgrest
                .from("patients")
                .update(patient) {
                    select()
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeSingle<Patient>()

            Result.Success(updatedPatient)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update patient")
        }
    }

    override suspend fun deletePatient(id: String): Result<Unit> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            postgrest
                .from("patients")
                .delete {
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete patient")
        }
    }

    override suspend fun searchPatients(query: String): Result<List<Patient>> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val patients = postgrest
                .from("patients")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        ilike("name", "%$query%")
                    }
                }
                .decodeList<Patient>()

            Result.Success(patients)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to search patients")
        }
    }

    override suspend fun getMedicalProcedureStats(): Result<List<MedicalProcedureStats>> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val patients = postgrest
                .from("patients")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<Patient>()

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
                .filter { it.medicalProcedure != null && it.medicalProcedure.isNotEmpty() }
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
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch medical procedure stats")
        }
    }

    override suspend fun uploadPatientImage(imageUri: Uri, oldImageUrl: String?): Result<String> {
        return try {
            val uploadResult = patientImageManager.uploadPatientImage(imageUri, oldImageUrl)
            
            when (uploadResult) {
                is Result.Success -> {
                    Result.Success(uploadResult.data)
                }
                is Result.Error -> {
                    Result.Error(uploadResult.message ?: "Failed to upload patient image")
                }
                else -> {
                    Result.Error("Unknown error occurred during upload")
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to upload patient image")
        }
    }
}
