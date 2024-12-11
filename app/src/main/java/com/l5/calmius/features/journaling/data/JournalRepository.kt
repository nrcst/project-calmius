package com.l5.calmius.feature.journaling.data

import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {
    fun getAllJournals(): Flow<List<JournalEntity>> {
        return journalDao.getAllJournals()
    }

    suspend fun getJournalById(journalId: Long): JournalEntity {
        return journalDao.getJournalById(journalId)
    }

    suspend fun insertJournal(journal: JournalEntity) {
        journalDao.insertJournal(journal)
    }

    suspend fun updateJournal(journal: JournalEntity) {
        journalDao.updateJournal(journal)
    }

    suspend fun deleteJournal(journal: JournalEntity) {
        journalDao.deleteJournal(journal)
    }
}