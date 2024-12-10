import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MeditationViewModel(
    private val repository: MeditationRepository
) : ViewModel() {

    private val _meditations = MutableStateFlow<List<Meditation>>(emptyList())
    val meditations = _meditations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadMeditations(type: MeditationType? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = if (type != null) {
                repository.getMeditationsByType(type)
            } else {
                repository.getMeditations()
            }

            result.fold(
                onSuccess = { _meditations.value = it },
                onFailure = { _error.value = it.message }
            )
            _isLoading.value = false
        }
    }

    fun uploadMeditation(
        name: String,
        description: String,
        duration: Int,
        type: MeditationType,
        audioFile: Uri
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.uploadMeditation(name, description, duration, type, audioFile)
                .fold(
                    onSuccess = { loadMeditations() },
                    onFailure = { _error.value = it.message }
                )
            _isLoading.value = false
        }
    }
}