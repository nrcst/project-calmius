package com.l5.calmius.feature.journaling.data

class JournalRepository(private val journalDao: JournalDao) {
    suspend fun getAllJournals(): List<JournalEntity> {
        return journalDao.getAllJournals()
    }

    suspend fun getJournalById(journalId: Long): JournalEntity {
        return journalDao.getJournalById(journalId)
    }

    suspend fun insertJournal(journal: JournalEntity) {
        journalDao.insert(journal)
    }

    suspend fun updateJournal(journal: JournalEntity) {
        journalDao.update(journal)
    }

    suspend fun deleteJournal(journal: JournalEntity) {
        journalDao.delete(journal)
    }
}