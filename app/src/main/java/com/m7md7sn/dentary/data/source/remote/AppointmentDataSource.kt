package com.m7md7sn.dentary.data.source.remote

import com.m7md7sn.dentary.data.model.Appointment
import com.m7md7sn.dentary.data.model.CreateAppointmentRequest
import com.m7md7sn.dentary.data.model.UpdateAppointmentRequest
import com.m7md7sn.dentary.domain.model.DataError
import com.m7md7sn.dentary.utils.Result

interface AppointmentDataSource {
    suspend fun getAllAppointments(): Result<List<Appointment>, DataError>
    suspend fun getAppointmentById(id: String): Result<Appointment, DataError>
    suspend fun getAppointmentsByPatient(patientId: String): Result<List<Appointment>, DataError>
    suspend fun getAppointmentsByDate(date: String): Result<List<Appointment>, DataError>
    suspend fun createAppointment(request: CreateAppointmentRequest): Result<Appointment, DataError>
    suspend fun updateAppointment(id: String, request: UpdateAppointmentRequest): Result<Appointment, DataError>
    suspend fun deleteAppointment(id: String): Result<Unit, DataError>
    suspend fun getAppointmentsUpdatedAfter(timestamp: String): Result<List<Appointment>, DataError>
}