package com.m7md7sn.dentary.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m7md7sn.dentary.data.model.Patient

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey
    val id: String,
    val userId: String?,
    val name: String,
    val phoneNumber: String?,
    val email: String?,
    val age: Int?,
    val address: String?,
    val gender: String?,
    val medicalHistory: String?,
    val medicalProcedure: String?,
    val image: String?,
    val lastVisitDate: String?,
    val createdAt: String?,
    val updatedAt: String?,
    val isSynced: Boolean = true
)

fun PatientEntity.toDomain(): Patient {
    return Patient(
        id = id,
        userId = userId,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        age = age,
        address = address,
        gender = gender,
        medicalHistory = medicalHistory,
        medicalProcedure = medicalProcedure,
        image = image,
        lastVisitDate = lastVisitDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Patient.toEntity(isSynced: Boolean = true): PatientEntity {
    return PatientEntity(
        id = id,
        userId = userId,
        name = name,
        phoneNumber = phoneNumber,
        email = email,
        age = age,
        address = address,
        gender = gender,
        medicalHistory = medicalHistory,
        medicalProcedure = medicalProcedure,
        image = image,
        lastVisitDate = lastVisitDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isSynced = isSynced
    )
}