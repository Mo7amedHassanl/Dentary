package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Appointment
import com.m7md7sn.dentary.data.model.CreateAppointmentRequest
import com.m7md7sn.dentary.data.model.UpdateAppointmentRequest
import com.m7md7sn.dentary.utils.Result
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import javax.inject.Inject

class AppointmentDataSourceImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) : AppointmentDataSource {

    override suspend fun getAllAppointments(): Result<List<Appointment>> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val appointments = postgrest.from("appointments")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                    }
                    order(column = "appointment_date", order = Order.DESCENDING)
                }
                .decodeList<Appointment>()

            Result.Success(appointments)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch appointments")
        }
    }

    override suspend fun getAppointmentById(id: String): Result<Appointment> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val appointment = postgrest.from("appointments")
                .select {
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeSingle<Appointment>()

            Result.Success(appointment)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch appointment")
        }
    }

    override suspend fun getAppointmentsByPatient(patientId: String): Result<List<Appointment>> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val appointments = postgrest.from("appointments")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        eq("patient_id", patientId)
                    }
                    order(column = "appointment_date", order = Order.ASCENDING)
                }
                .decodeList<Appointment>()

            Result.Success(appointments)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch patient appointments")
        }
    }

    override suspend fun getAppointmentsByDate(date: String): Result<List<Appointment>> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val appointments = postgrest.from("appointments")
                .select {
                    filter {
                        eq("user_id", currentUserId)
                        gte("appointment_date", "${date}T00:00:00")
                        lt("appointment_date", "${date}T23:59:59")
                    }
                    order(column = "appointment_date", order = Order.ASCENDING)
                }
                .decodeList<Appointment>()

            Result.Success(appointments)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to fetch appointments by date")
        }
    }

    override suspend fun createAppointment(request: CreateAppointmentRequest): Result<Appointment> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val appointmentData = mapOf(
                "user_id" to currentUserId,
                "patient_id" to request.patientId,
                "title" to request.title,
                "description" to request.description,
                "appointment_date" to request.appointmentDate,
                "duration_minutes" to request.durationMinutes
            )

            val appointment = postgrest.from("appointments")
                .insert(appointmentData) {
                    select()
                }
                .decodeSingle<Appointment>()

            Result.Success(appointment)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to create appointment")
        }
    }

    override suspend fun updateAppointment(id: String, request: UpdateAppointmentRequest): Result<Appointment> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            val appointment = postgrest.from("appointments")
                .update(request) {
                    select()
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }
                .decodeSingle<Appointment>()

            Result.Success(appointment)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update appointment")
        }
    }

    override suspend fun deleteAppointment(id: String): Result<Unit> {
        return try {
            val currentUserId = auth.currentUserOrNull()?.id
                ?: return Result.Error("User not authenticated")

            postgrest.from("appointments")
                .delete {
                    filter {
                        eq("id", id)
                        eq("user_id", currentUserId)
                    }
                }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to delete appointment")
        }
    }
}
