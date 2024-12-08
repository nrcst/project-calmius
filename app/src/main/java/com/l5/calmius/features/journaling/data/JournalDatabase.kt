package com.l5.calmius.features.journaling.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.l5.calmius.feature.journaling.data.JournalDao
import com.l5.calmius.feature.journaling.data.JournalEntity

@Database(entities = [JournalEntity::class], version = 1, exportSchema = false)
abstract class JournalDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var Instance: JournalDatabase? = null

        fun getDatabase(context: Context): JournalDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, JournalDatabase::class.java, "journal_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}