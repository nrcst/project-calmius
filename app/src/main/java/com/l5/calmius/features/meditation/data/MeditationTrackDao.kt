package com.l5.calmius.features.meditation.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MeditationTrackDao {
    @Insert
    suspend fun insertTrack(track: List<MeditationTrack>)

    @Query("SELECT * FROM meditation_tracks WHERE meditationType = :type")
    suspend fun getTracksByType(type: MeditationType): List<MeditationTrack>

    @Query("SELECT * FROM meditation_tracks WHERE id = :id")
    suspend fun getTrackById(id: Int): MeditationTrack?
}
