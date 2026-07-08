package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Appointment
import com.m7md7sn.dentary.data.model.CreateAppointmentRequest
import com.m7md7sn.dentary.data.model.UpdateAppointmentRequest
import com.m7md7sn.dentary.data.util.toDataError
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import javax.inject.Inject

class AppointmentDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) : AppointmentDataSource {

    override suspend fun getAllAppointments(): Result<List<Appointment>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val appointments = postgrest
                .from("appointments")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                    }
                }
                .decodeList<Appointment>()

            Result.Success(appointments)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getAppointmentById(id: String): Result<Appointment, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val appointment = postgrest
                .from("appointments")
                .select {
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeSingle<Appointment>()

            Result.Success(appointment)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getAppointmentsByPatient(patientId: String): Result<List<Appointment>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val appointments = postgrest
                .from("appointments")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        eq("patient_id", patientId)
                    }
                }
                .decodeList<Appointment>()

            Result.Success(appointments)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun getAppointmentsByDate(date: String): Result<List<Appointment>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val appointments = postgrest
                .from("appointments")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        eq("appointment_date", date)
                    }
                }
                .decodeList<Appointment>()

            Result.Success(appointments)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun createAppointment(request: CreateAppointmentRequest): Result<Appointment, DataError> {
        return try {
            val createdAppointment = postgrest
                .from("appointments")
                .insert(request) {
                    select()
                }
                .decodeSingle<Appointment>()

            Result.Success(createdAppointment)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun updateAppointment(id: String, request: UpdateAppointmentRequest): Result<Appointment, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val updatedAppointment = postgrest
                .from("appointments")
                .update(request) {
                    select()
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeSingle<Appointment>()

            Result.Success(updatedAppointment)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }

    override suspend fun deleteAppointment(id: String): Result<Unit, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            postgrest
                .from("appointments")
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

    override suspend fun getAppointmentsUpdatedAfter(timestamp: String): Result<List<Appointment>, DataError> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error(DataError.Auth.SESSION_EXPIRED, "User not authenticated")

            val appointments = postgrest
                .from("appointments")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        gt("updated_at", timestamp)
                    }
                }
                .decodeList<Appointment>()

            Result.Success(appointments)
        } catch (e: Exception) {
            Result.Error(e.toDataError(), e.message)
        }
    }
}