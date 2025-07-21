package com.m7md7sn.dentary.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.m7md7sn.dentary.R

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)
val Alexandria = GoogleFont("Alexandria")
val Montserrat = GoogleFont("Montserrat")

val AlexandriaBlack = FontFamily(
    Font(
        googleFont = Alexandria,
        fontProvider = googleFontProvider,
        weight = FontWeight.Black,
        style = FontStyle.Normal
    )
)

val AlexandriaMedium = FontFamily(
    Font(
        googleFont = Alexandria,
        fontProvider = googleFontProvider,
        weight = FontWeight.Medium,
        style = FontStyle.Normal
    )
)

val AlexandriaRegular = FontFamily(
    Font(
        googleFont = Alexandria,
        fontProvider = googleFontProvider,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
)

val AlexandriaBold = FontFamily(
    Font(
        googleFont = Alexandria,
        fontProvider = googleFontProvider,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    )
)


val AlexandriaSemiBold = FontFamily(
    Font(
        googleFont = Alexandria,
        fontProvider = googleFontProvider,
        weight = FontWeight.SemiBold,
        style = FontStyle.Normal
    )
)

val AlexandriaExtraBold = FontFamily(
    Font(
        googleFont = Alexandria,
        fontProvider = googleFontProvider,
        weight = FontWeight.ExtraBold,
        style = FontStyle.Normal
    )
)

val MontserratRegular = FontFamily(
    Font(
        googleFont = Montserrat,
        fontProvider = googleFontProvider,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
)

val MontserratMedium = FontFamily(
    Font(
        googleFont = Montserrat,
        fontProvider = googleFontProvider,
        weight = FontWeight.Medium,
        style = FontStyle.Normal
    )
)

val MontserratBold = FontFamily(
    Font(
        googleFont = Montserrat,
        fontProvider = googleFontProvider,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    )
)

val MontserratBlack = FontFamily(
    Font(
        googleFont = Montserrat,
        fontProvider = googleFontProvider,
        weight = FontWeight.Black,
        style = FontStyle.Normal
    )
)

val MontserratLight = FontFamily(
    Font(
        googleFont = Montserrat,
        fontProvider = googleFontProvider,
        weight = FontWeight.Light,
        style = FontStyle.Normal
    )
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)