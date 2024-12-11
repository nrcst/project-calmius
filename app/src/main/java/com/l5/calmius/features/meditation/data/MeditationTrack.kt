package com.l5.calmius.features.meditation.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "meditation_tracks")
data class MeditationTrack(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val duration: Int,
    val meditationType: MeditationType,
    val audioPath: String
)

class Converters {
    @TypeConverter
    fun fromMeditationType(value: MeditationType): String {
        return value.name
    }

    @TypeConverter
    fun toMeditationType(value: String): MeditationType {
        return MeditationType.valueOf(value)
    }
}
