package com.l5.calmius.ui.theme

import androidx.compose.ui.graphics.Color
import com.l5.calmius.features.meditation.data.MeditationType

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val Blue50 = Color(0xFFF6FBFF)
val Blue75 = Color(0xFFDAEFFF)
val Blue100 = Color(0xFFCBE8FF)
val Blue200 = Color(0xFFB4DFFF)
val Blue300 = Color(0xFFA5D8FF)
val Blue400 = Color(0xFF7397B3)
val Blue500 = Color(0xFF65849C)
val TextFieldBackground = Color(0xFFE9F1FC)

object MeditationColors {
    val Mood = Color(0xFFDDF0E7)
    val Calm = Color(0xFFE9F1FC)
    val Memories = Color(0xFFE7DDF0)
    val Energic = Color(0xFFF0EEDD)

    fun getColorByType(type: MeditationType): Color {
        return when (type) {
            MeditationType.MOOD -> Mood
            MeditationType.CALM -> Calm
            MeditationType.MEMORIES -> Memories
            MeditationType.ENERGIC -> Energic
        }
    }
}