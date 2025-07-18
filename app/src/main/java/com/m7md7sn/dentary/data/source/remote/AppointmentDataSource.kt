package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Appointment
import com.m7md7sn.dentary.data.model.CreateAppointmentRequest
import com.m7md7sn.dentary.data.model.UpdateAppointmentRequest
import com.m7md7sn.dentary.utils.Result

interface AppointmentDataSource {
    suspend fun getAllAppointments(): Result<List<Appointment>>
    suspend fun getAppointmentById(id: String): Result<Appointment>
    suspend fun getAppointmentsByPatient(patientId: String): Result<List<Appointment>>
    suspend fun getAppointmentsByDate(date: String): Result<List<Appointment>>
    suspend fun createAppointment(request: CreateAppointmentRequest): Result<Appointment>
    suspend fun updateAppointment(id: String, request: UpdateAppointmentRequest): Result<Appointment>
    suspend fun deleteAppointment(id: String): Result<Unit>
}
