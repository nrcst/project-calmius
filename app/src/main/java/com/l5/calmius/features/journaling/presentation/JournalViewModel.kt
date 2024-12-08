package com.l5.calmius.feature.journaling.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.data.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
            withContext(Dispatchers.IO) {
                _journals.value = journalRepository.getAllJournals()
            }
        }
    }

    fun getJournalById(journalId: Long, callback: (JournalEntity?) -> Unit) {
        viewModelScope.launch {
            val journal = withContext(Dispatchers.IO) {
                journalRepository.getJournalById(journalId)
            }
            callback(journal)
        }
    }

    fun saveJournal(journal: JournalEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (journal.id == 0L) {
                    journalRepository.insertJournal(journal)
                } else {
                    journalRepository.updateJournal(journal)
                }
                loadJournals()
            }
        }
    }

    fun deleteJournal(journal: JournalEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                journalRepository.deleteJournal(journal)
                loadJournals()
            }
        }
    }
}