package com.m7md7sn.dentary.data.source.local

import androidx.room.TypeConverter
import com.m7md7sn.dentary.data.model.AppointmentStatus

class Converters {
    @TypeConverter
    fun fromAppointmentStatus(status: AppointmentStatus): String {
        return status.name
    }

    @TypeConverter
    fun toAppointmentStatus(name: String): AppointmentStatus {
        return try {
            AppointmentStatus.valueOf(name)
        } catch (e: IllegalArgumentException) {
            AppointmentStatus.SCHEDULED
        }
    }
}