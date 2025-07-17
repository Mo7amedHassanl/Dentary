package com.m7md7sn.dentary.data.model

data class Patient(
    val id: String,
    val name: String,
    val age: Int,
    val phoneNumber: String,
    val email: String? = null,
    val lastVisitDate: String? = null,
)
