package com.m7md7sn.dentary.data.source.remote

import android.net.Uri
import com.m7md7sn.dentary.data.model.CreatePatientRequest
import com.m7md7sn.dentary.data.model.MedicalProcedureStats
import com.m7md7sn.dentary.data.model.Patient
import com.m7md7sn.dentary.data.repository.PatientImageManager
import com.m7md7sn.dentary.data.util.toDataError
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class PatientDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val patientImageManager: PatientImageManager
) : PatientDataSource {

    companion object {
        private val timestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

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

            val currentTime = timestampFormat.format(Date())

            // Create PatientInsert object with ID if it's not a temporary/empty one
            val createPatientRequest = CreatePatientRequest(
                id = if (patient.id.isNotBlank()) patient.id else null,
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

            val currentTime = timestampFormat.format(Date())
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

            val procedureCounts = patients
                .filter { it.medicalProcedure != null && it.medicalProcedure.isNotEmpty() }
                .groupBy { it.medicalProcedure!! }
                .map { (procedure, list) ->
                    MedicalProcedureStats(procedure, list.size)
                }

            Result.Success(procedureCounts)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getPatientsUpdatedAfter(timestamp: String): Result<List<Patient>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val patients = postgrest
                .from("patients")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        gt("updated_at", timestamp)
                    }
                }
                .decodeList<Patient>()

            Result.Success(patients)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun uploadPatientImage(imageUri: Uri, oldImageUrl: String?): Result<String, DataError> {
        val uploadResult = patientImageManager.uploadPatientImage(imageUri, oldImageUrl)
        return when (uploadResult) {
            is Result.Success -> Result.Success(uploadResult.data)
            is Result.Error -> Result.Error(uploadResult.error, uploadResult.message)
            is Result.Loading -> Result.Loading
        }
    }
}