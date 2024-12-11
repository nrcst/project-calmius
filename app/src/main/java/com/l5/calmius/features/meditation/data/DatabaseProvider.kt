// meditation/data/DatabaseProvider.kt
package meditation.data

import android.content.Context
import androidx.room.Room
import com.l5.calmius.features.meditation.data.AppDatabase
import kotlinx.coroutines.CoroutineScope

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "meditation_database"
            )
                .addCallback(AppDatabase.DatabaseCallback(scope))
                .build()
            INSTANCE = instance
            instance
        }
    }
}
