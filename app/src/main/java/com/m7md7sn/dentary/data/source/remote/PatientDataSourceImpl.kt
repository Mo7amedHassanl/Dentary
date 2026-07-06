package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.model.CreatePatientRequest
import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.data.repository.PatientImageManager
import com.m7md7sn.dentary.data.util.toDataError
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject
import android.net.Uri

import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class PatientDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val patientImageManager: PatientImageManager
) : PatientDataSource {

    override suspend fun getAllPatients(): Result<List<Patient>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

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
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getPatientById(id: String): Result<Patient, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

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
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun createPatient(patient: Patient): Result<Patient, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val currentTime = sdf.format(Date())

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
                image = patient.image,
                gender = patient.gender,
                lastVisitDate = currentTime
            )

            val createdPatient = postgrest
                .from("patients")
                .insert(createPatientRequest) {
                    select()
                }
                .decodeSingle<Patient>()

            Result.Success(createdPatient)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun updatePatient(id: String, patient: Patient): Result<Patient, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val currentTime = sdf.format(Date())
            val updatedPatientWithDate = patient.copy(lastVisitDate = currentTime)

            val updatedPatient = postgrest
                .from("patients")
                .update(updatedPatientWithDate) {
                    select()
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeSingle<Patient>()

            Result.Success(updatedPatient)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun deletePatient(id: String): Result<Unit, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

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
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun searchPatients(query: String): Result<List<Patient>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

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
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getMedicalProcedureStats(): Result<List<MedicalProcedureStats>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

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
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun uploadPatientImage(imageUri: Uri, oldImageUrl: String?): Result<String, DataError> {
        return try {
            val uploadResult = patientImageManager.uploadPatientImage(imageUri, oldImageUrl)
            
            when (uploadResult) {
                is Result.Success -> {
                    Result.Success(uploadResult.data)
                }
                is Result.Error -> {
                    Result.Error(uploadResult.error, uploadResult.message ?: "Failed to upload patient image")
                }
                else -> {
                    Result.Error(DataError.Network.UNKNOWN, "Unknown error occurred during upload")
                }
            }
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }
}
