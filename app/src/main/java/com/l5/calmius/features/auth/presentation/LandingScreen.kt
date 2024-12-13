package com.example.finalpapb.Auth.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.l5.calmius.R
import com.l5.calmius.features.auth.data.AuthViewModel

@Composable
fun LandingScreen(
    navController: NavController,authViewModel: AuthViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Calmius Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Welcome to",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff49454F)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Calmius",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xff49454F)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Support for all of life's",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "moments",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(220.dp))
            OutlinedButton(
                onClick = {
                    navController.navigate("signUp")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xff7397B3)
                ),
                border = BorderStroke(1.dp, Color(0xff7397B3)),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = "Create an account"
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    navController.navigate("signIn")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(Color(0xff7397B3)),
                shape = RoundedCornerShape(24.dp),
            ) {
                Text(
                    text = "Sign In"
                )
            }
        }
    }
}