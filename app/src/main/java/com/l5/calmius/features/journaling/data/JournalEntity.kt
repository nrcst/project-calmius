package com.l5.calmius.feature.journaling.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val title: String,
    val story: String,
    val gratitude: String,
    val imageUrl: String
)