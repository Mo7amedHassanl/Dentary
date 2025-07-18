package com.m7md7sn.dentary.data.repository

import com.m7md7sn.dentary.data.model.Appointment
import com.m7md7sn.dentary.data.model.CreateAppointmentRequest
import com.m7md7sn.dentary.data.model.UpdateAppointmentRequest
import com.m7md7sn.dentary.data.source.remote.AppointmentDataSource
import com.m7md7sn.dentary.utils.Result
import javax.inject.Inject

class AppointmentRepositoryImpl @Inject constructor(
    private val appointmentDataSource: AppointmentDataSource
) : AppointmentRepository {

    override suspend fun getAllAppointments(): Result<List<Appointment>> {
        return appointmentDataSource.getAllAppointments()
    }

    override suspend fun getAppointmentById(id: String): Result<Appointment> {
        return appointmentDataSource.getAppointmentById(id)
    }

    override suspend fun getAppointmentsByPatient(patientId: String): Result<List<Appointment>> {
        return appointmentDataSource.getAppointmentsByPatient(patientId)
    }

    override suspend fun getAppointmentsByDate(date: String): Result<List<Appointment>> {
        return appointmentDataSource.getAppointmentsByDate(date)
    }

    override suspend fun createAppointment(request: CreateAppointmentRequest): Result<Appointment> {
        return appointmentDataSource.createAppointment(request)
    }

    override suspend fun updateAppointment(id: String, request: UpdateAppointmentRequest): Result<Appointment> {
        return appointmentDataSource.updateAppointment(id, request)
    }

    override suspend fun deleteAppointment(id: String): Result<Unit> {
        return appointmentDataSource.deleteAppointment(id)
    }
}
