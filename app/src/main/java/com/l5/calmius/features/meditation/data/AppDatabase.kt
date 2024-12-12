package com.l5.calmius.features.meditation.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.l5.calmius.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [MeditationTrack::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun meditationTrackDao(): MeditationTrackDao

    internal class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.meditationTrackDao())
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meditation_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

suspend fun populateDatabase(dao: MeditationTrackDao) {
    val tracks = listOf(
        MeditationTrack(title = "Become as calm as the wind blow.", brief = "Drift peacefully, like a calm gentle breeze.", description = "Ease your mind and relax. Practice your mindfulness and self-awareness. Train your attention and achieve emotionally calm and stable state of mind.", duration = 5, durationMillis = (5 * 60 + 9) * 1000, meditationType = MeditationType.MOOD, resourceId = R.raw.mood01),
        MeditationTrack(title = "Relaxing 2", brief = "this is brief", description = "Relaxing music for mood desc", duration = 5, durationMillis = (5 * 60 + 0) * 1000, meditationType = MeditationType.ENERGIC, resourceId = R.raw.mood04),
    )
    dao.insertTrack(tracks)
}