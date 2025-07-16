package com.m7md7sn.dentary.data.model


data class LoginCredentials(
    val email: String,
    val password: String
)

data class SignUpCredentials(
    val email: String,
    val password: String,
    val username: String,
    val clinicName: String,
    val phoneNumber: String,
    val address: String
)
