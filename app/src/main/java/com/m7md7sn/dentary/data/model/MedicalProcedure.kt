package com.m7md7sn.dentary.data.model

import com.m7md7sn.dentary.R

enum class MedicalProcedure(val resId: Int, val dbValue: String) {
    NORMAL_FILLING(R.string.procedure_normal_filling, "حشو عادي"),
    SURGERY(R.string.procedure_surgery, "جراحة"),
    CLEANING(R.string.procedure_cleaning, "تنظيف جير"),
    ROOT_CANAL(R.string.procedure_root_canal, "حشو عصب"),
    REMOVABLE_PROSTHESIS(R.string.procedure_removable_prosthesis, "تركيبات متحركة"),
    FIXED_PROSTHESIS(R.string.procedure_fixed_prosthesis, "تركيبات ثابتة");

    companion object {
        fun fromDbValue(value: String?): MedicalProcedure? {
            return entries.find { it.dbValue == value }
        }
        
        fun getAllDbValues(): List<String> = entries.map { it.dbValue }
    }
}
