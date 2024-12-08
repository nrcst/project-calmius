package com.l5.calmius.feature.journaling.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface JournalDao {
    @Insert
    suspend fun insertJournal(journal: JournalEntity)

    @Update
    suspend fun updateJournal(journal: JournalEntity)

    @Delete
    suspend fun deleteJournal(journal: JournalEntity)

    @Query("SELECT * FROM journals")
    fun getAllJournals(): List<JournalEntity>

    @Query("SELECT * FROM journals WHERE id = :journalId")
    fun getJournalById(journalId: Long): JournalEntity
}