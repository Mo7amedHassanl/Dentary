package com.m7md7sn.dentary.data.model

import kotlinx.serialization.Serializable
 
@Serializable
data class MedicalProcedureStats(
    val procedure: String,
    val count: Int
) 