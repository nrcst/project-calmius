import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class MeditationRepository {
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage.reference
    private val meditationsCollection = firestore.collection("meditations")

    suspend fun uploadMeditation(
        name: String,
        description: String,
        duration: Int,
        type: MeditationType,
        audioFile: Uri
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val audioFileName = "meditations/${UUID.randomUUID()}.mp3"
            val audioRef = storage.child(audioFileName)
            audioRef.putFile(audioFile).await()

            val audioUrl = audioRef.downloadUrl.await().toString()

            val meditation = Meditation(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                duration = duration,
                mood = type,
                audioPath = audioFileName
            )

            meditationsCollection.document(meditation.id).set(meditation).await()

            Result.success(meditation.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMeditations(): Result<List<Meditation>> = withContext(Dispatchers.IO) {
        try {
            val meditations = meditationsCollection.get().await()
                .toObjects(Meditation::class.java)
            Result.success(meditations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMeditationsByType(type: MeditationType): Result<List<Meditation>> =
        withContext(Dispatchers.IO) {
            try {
                val meditations = meditationsCollection
                    .whereEqualTo("mood", type)
                    .get()
                    .await()
                    .toObjects(Meditation::class.java)
                Result.success(meditations)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun getMeditationAudioUrl(audioPath: String): Result<String> =
        withContext(Dispatchers.IO) {
            try {
                val url = storage.child(audioPath).downloadUrl.await().toString()
                Result.success(url)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}