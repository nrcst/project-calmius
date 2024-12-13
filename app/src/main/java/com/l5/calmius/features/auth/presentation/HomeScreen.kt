package com.l5.calmius.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.l5.calmius.features.auth.data.AuthState
import com.l5.calmius.features.auth.data.AuthViewModel

@Composable
fun HomeScreen(
    navController: NavController,authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("landingScreen")
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Good Morning!",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(Color(0xffF0EEDD))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "What is Calmius ?",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF65849C)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "‘Calmius’ is a meditation app that offers a solution to mental health problems by providing a platform that is easy to use, affordable and supports users in managing depression.",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sessions for you",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                modifier = Modifier.padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(3) {
                    SessionCard(
                        title = "Feeling the Breath",
                        description = "Use your breath to wind down and get ready for sleep.",
                        footer = "Meditate • 4 min • Live mindfully",
                        backgroundColor = Color(0xFFDDF0E7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Our picks",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(3) {
                    SessionCard(
                        title = "Starry Night",
                        description = "Say goodnight to overthinking using the starry sky as your guide to sleep.",
                        footer = "Meditate • 6 min • Recharge your body",
                        backgroundColor = Color(0xFFE7DDF0)
                    )
                }
            }
            
            Button(
                onClick = {
                    authViewModel.signOut()
                },
            ){
                Text(
                    text = "Sign Out"
                )
            }
        }
    }
}

@Composable
fun SessionCard(
    title: String,
    description: String,
    footer: String,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .heightIn(max = 160.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF65849C)
                )

                Text(
                    text = description,
                    fontSize = 15.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.Start)
                )

                Text(
                    text = footer,
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun HomePagePreview() {
//    HomePage()
//}