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
        // mood
        MeditationTrack(title = "Lift Your Spirit with Joyful Sounds.", brief = "Uplift your mood and feel positive energy.", description = "Tune into happiness and let your spirit soar with this cheerful melody.", duration = 5, durationMillis = (5 * 60 + 9) * 1000, meditationType = MeditationType.MOOD, resourceId = R.raw.mood01),
        MeditationTrack(title = "Embrace Gratitude and Joy.", brief = "Focus on gratitude and bring positive energy into your life.", description = "Cultivate joy and gratitude, shifting your mindset towards abundance.", duration = 5, durationMillis = (5 * 60 + 0) * 1000, meditationType = MeditationType.MOOD, resourceId = R.raw.mood04),

        // calm
        MeditationTrack(title = "Become as calm as the wind blow.", brief = "Drift peacefully, like a calm gentle breeze.", description = "Ease your mind and relax. Practice your mindfulness and self-awareness. Train your attention and achieve emotionally calm and stable state of mind.", duration = 3, durationMillis = (3 * 60 + 10) * 1000, meditationType = MeditationType.CALM, resourceId = R.raw.calm01),
        MeditationTrack(title = "Soft Breeze, Calm Heart.", brief = "Feel the gentle breeze bringing peace and relaxation.", description = "Close your eyes and breathe in the soft, calming sounds of nature.", duration = 3, durationMillis = (3 * 60 + 0) * 1000, meditationType = MeditationType.CALM, resourceId = R.raw.calm02),

        // memories
        MeditationTrack(title = "Reconnect with Old Memories.", brief = "Dive into your past and relive cherished moments.", description = "Let the sounds guide you back to peaceful and nostalgic memories.", duration = 2, durationMillis = (2 * 60 + 9) * 1000, meditationType = MeditationType.MEMORIES, resourceId = R.raw.memories01),
        MeditationTrack(title = "Cherished Moments, Heartfelt Memories.", brief = "Recall your happiest moments with a soft melody.", description = "Travel back in time and let this track evoke joyful memories of your life.", duration = 1, durationMillis = (1 * 60 + 6) * 1000, meditationType = MeditationType.MEMORIES, resourceId = R.raw.memories01),

        // energic
        MeditationTrack(title = "Awaken Your Energy Within.", brief = "Ignite your inner power and boost your motivation.", description = "Feel the surge of energy and drive as this track awakens your inner strength.", duration = 3, durationMillis = (3 * 60 + 15) * 1000, meditationType = MeditationType.ENERGIC, resourceId = R.raw.energic01),
        MeditationTrack(title = "Rise and Shine with Vitality.", brief = "Embrace the day with energy and excitement.", description = "Start your day with renewed energy and a positive, vibrant mindset.", duration = 2, durationMillis = (2 * 60 + 4) * 1000, meditationType = MeditationType.ENERGIC, resourceId = R.raw.energic02),
    )
    dao.insertTrack(tracks)
}