package com.l5.calmius.features.journaling.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.feature.journaling.presentation.JournalViewModel
import com.l5.calmius.ui.theme.Blue75
import com.l5.calmius.ui.theme.Typography
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun JournalListItem(
    navController: NavController,
    journal: JournalEntity,
    journalViewModel: JournalViewModel,
) {
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    val formattedDate = journal?.date?.let {
        dateFormat.format(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it)!!)
    } ?: "Unknown Date"

    Card(
        modifier = Modifier
            .padding(14.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Blue75),
        onClick = {
            navController.navigate("journalDetail/${journal.id}")
        }
    ) {
        Column(
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 10.dp)
            ){
                Text(
                    text = journal.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp)
            ){
                Text(
                    text = formattedDate,
                    style = Typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
            ){
                AsyncImage(
                    model = journal.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp)
//                        .clip(RoundedCornerShape(8.dp))
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Text(
                    text = journal.story,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.bodyMedium
                )
            }
        }
    }
}