package com.l5.calmius.ui.theme

import android.R.id.bold
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.l5.calmius.R

val Roboto = FontFamily(
    Font(R.font.roboto_black, FontWeight.Bold),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_regular, FontWeight.Normal)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp
    ),
    displayMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp
    ),
    displaySmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)