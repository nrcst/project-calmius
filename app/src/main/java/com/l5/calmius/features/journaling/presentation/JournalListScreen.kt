package com.l5.calmius.feature.journaling.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.l5.calmius.feature.journaling.data.JournalEntity
import com.l5.calmius.features.journaling.presentation.JournalListItem
import com.l5.calmius.ui.theme.Typography

@Composable
fun JournalListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: JournalViewModel,
) {
    val journals by viewModel.journals.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("journalAdd")
                },
                shape = CircleShape,
                containerColor = Color.Gray,
                contentColor = Color.White,
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(start = 32.dp, end = 32.dp, top = 12.dp)
        ) {
            Text(
                text = "My Journal",
                style = Typography.displayLarge,
                fontWeight = Typography.displayMedium.fontWeight,
                modifier = Modifier.padding(start = 10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Everydays is part of your life",
                style = Typography.bodyLarge,
                fontWeight = Typography.bodySmall.fontWeight,
                modifier = Modifier.padding(start = 10.dp),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (journals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Let's write your stories!",
                        style = Typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(journals) { journal ->
                        JournalListItem(
                            journal = journal,
                            journalViewModel = viewModel,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}