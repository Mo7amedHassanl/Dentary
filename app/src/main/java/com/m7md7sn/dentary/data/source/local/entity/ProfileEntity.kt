package com.m7md7sn.dentary.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.m7md7sn.dentary.data.model.Profile

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey
    val id: String,
    val fullName: String?,
    val clinicName: String?,
    val phoneNumber: String?,
    val clinicAddress: String?,
    val specialization: String?,
    val profilePicture: String?,
    val clinicLogo: String?,
    val updatedAt: String?,
    val isSynced: Boolean = true,
    val lastUpdatedLocal: Long = System.currentTimeMillis(),
    val isDeletedLocally: Boolean = false
)

fun ProfileEntity.toDomain(): Profile {
    return Profile(
        id = this.id,
        fullName = this.fullName,
        clinicName = this.clinicName,
        phoneNumber = this.phoneNumber,
        clinicAddress = this.clinicAddress,
        specialization = this.specialization,
        profilePicture = this.profilePicture,
        clinicLogo = this.clinicLogo,
        updatedAt = this.updatedAt
    )
}

fun Profile.toEntity(
    isSynced: Boolean = true,
    lastUpdatedLocal: Long = System.currentTimeMillis(),
    isDeletedLocally: Boolean = false
): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        fullName = this.fullName,
        clinicName = this.clinicName,
        phoneNumber = this.phoneNumber,
        clinicAddress = this.clinicAddress,
        specialization = this.specialization,
        profilePicture = this.profilePicture,
        clinicLogo = this.clinicLogo,
        updatedAt = this.updatedAt,
        isSynced = isSynced,
        lastUpdatedLocal = lastUpdatedLocal,
        isDeletedLocally = isDeletedLocally
    )
}