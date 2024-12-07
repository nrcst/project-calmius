package com.l5.calmius.feature.journaling.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.data.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JournalViewModel(
    private val journalRepository: JournalRepository
) : ViewModel() {
    private val _journals = MutableStateFlow<List<JournalEntity>>(emptyList())
    val journals: StateFlow<List<JournalEntity>> = _journals.asStateFlow()

    init {
        loadJournals()
    }

    fun loadJournals() {
        viewModelScope.launch {
            _journals.value = journalRepository.getAllJournals()
        }
    }

    suspend fun getJournalById(journalId: Long): JournalEntity {
        return journalRepository.getJournalById(journalId)
    }

    fun saveJournal(journal: JournalEntity) {
        viewModelScope.launch {
            if (journal.id == 0L) {
                journalRepository.insertJournal(journal)
            } else {
                journalRepository.updateJournal(journal)
            }
            loadJournals()
        }
    }

    fun deleteJournal(journal: JournalEntity) {
        viewModelScope.launch {
            journalRepository.deleteJournal(journal)
            loadJournals()
        }
    }
}