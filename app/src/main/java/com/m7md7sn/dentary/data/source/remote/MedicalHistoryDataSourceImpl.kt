package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.CreateMedicalHistoryRequest
import com.m7md7sn.dentary.data.model.MedicalHistory
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

class MedicalHistoryDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) : MedicalHistoryDataSource {

    companion object {
        private val timestampFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    override suspend fun createMedicalHistory(request: CreateMedicalHistoryRequest): Result<MedicalHistory, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val medicalHistory = postgrest
                .from("medical_histories")
                .insert(request) { select() }
                .decodeSingle<MedicalHistory>()

            Result.Success(medicalHistory)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getMedicalHistoriesByPatient(patientId: String): Result<List<MedicalHistory>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val histories = postgrest
                .from("medical_histories")
                .select {
                    filter { eq("patient_id", patientId) }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<MedicalHistory>()

            Result.Success(histories)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun updateMedicalHistory(id: String, description: String): Result<MedicalHistory, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val updated = postgrest
                .from("medical_histories")
                .update(mapOf("description" to description)) {
                    select()
                    filter { eq("id", id) }
                }
                .decodeSingle<MedicalHistory>()

            Result.Success(updated)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun deleteMedicalHistory(id: String): Result<Unit, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            postgrest
                .from("medical_histories")
                .delete { filter { eq("id", id) } }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getMedicalHistoriesUpdatedAfter(timestamp: String): Result<List<MedicalHistory>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val histories = postgrest
                .from("medical_histories")
                .select {
                    filter { gt("updated_at", timestamp) }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<MedicalHistory>()

            Result.Success(histories)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }
}
