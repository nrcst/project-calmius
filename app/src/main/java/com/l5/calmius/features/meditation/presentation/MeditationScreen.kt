import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MeditationScreen(
    viewModel: MeditationViewModel = viewModel()
) {
    val meditations by viewModel.meditations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMeditations()
    }

    Column {
        // Your existing UI code
        if (isLoading) {
            CircularProgressIndicator()
        }

        error?.let { errorMessage ->
            Text (
                text = errorMessage,
            )
        }

        LazyColumn {
            items(meditations) { meditation ->
                MeditationItem(meditation = meditation)
            }
        }
    }
}

@Composable
fun MeditationItem(meditation: Meditation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = meditation.name)
            Text(text = meditation.description)
            Text(text = "${meditation.duration} minutes")
            Text(text = "Type: ${meditation.mood}")
        }
    }
}