package com.l5.calmius.feature.journaling.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.l5.calmius.feature.journaling.data.JournalRepository

class JournalViewModelFactory(
    private val journalRepository: JournalRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(journalRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}