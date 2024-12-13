package com.l5.calmius.features.auth.presentation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.l5.calmius.R
import com.l5.calmius.features.auth.data.AuthState
import com.l5.calmius.features.auth.data.AuthViewModel
import com.l5.calmius.features.auth.data.State

@Composable
fun SignUpScreen(
    navController: NavController, authViewModel: AuthViewModel, state: State, googleSignIn: () -> Unit
) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.SignupSuccess -> {
                navController.navigate("profile") {
                    popUpTo("signup") { inclusive = true }
                }
                authViewModel.resetAuthState()
            }
            is AuthState.Error -> {
                Toast.makeText(context,
                    (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Sign Up",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff49454F)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Create an account to access meditations, sleep,",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "sound, music to help you focus and more.",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?",
                        color = Color.Gray,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    TextButton(
                        onClick = {
                            navController.navigate("signIn") {
                                popUpTo("signUp") { inclusive = true }
                            }
                        },
                        contentPadding = PaddingValues(start = 2.dp)
                    ) {
                        Text(
                            text = "Sign In",
                            color = Color(0xff65849C),
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
                var fullName by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {fullName = it},
                    label = { Text("Full Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    shape = RoundedCornerShape(10.dp)
                )
                var phoneNum by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = phoneNum,
                    onValueChange = {phoneNum = it},
                    label = { Text("Phone Number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    shape = RoundedCornerShape(10.dp)
                )
                var email by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = email,
                    onValueChange = {email = it},
                    label = { Text("Email Address") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(10.dp)
                )
                var password by remember { mutableStateOf("") }
                OutlinedTextField(
                    value = password,
                    onValueChange = {password = it},
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(10.dp)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Button(
                    onClick = {
                        authViewModel.signUp(fullName = fullName, phoneNum = phoneNum, email = email, password = password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xff7397B3))
                ) {
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Link an account to log in faster in the future",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SocialButton(
                        icon = R.drawable.google,
                        onClick = {
                            googleSignIn()
                        }
                    )
                    SocialButton(
                        icon = R.drawable.facebook,
                        onClick = {  }
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    text = "By continuing, you agree to Calmius's",
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { /*navController.navigate("register") */},
                        contentPadding = PaddingValues(start = 4.dp, end = 4.dp)
                    ) {
                        Text(
                            text = "Terms And Condition",
                            color = Color(0xff65849C),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                    Text(
                        text = "and",
                        color = Color.Gray,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    TextButton(
                        onClick = { /*navController.navigate("register") */},
                        contentPadding = PaddingValues(start = 4.dp)
                    ) {
                        Text(
                            text = "Privacy Policy",
                            color = Color(0xff65849C),
                            fontSize = 14.sp,
                            fontFamily = FontFamily.SansSerif,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun SocialButton(
    icon: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Social media icon",
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SignUpPagePreview() {
//    SignUpPage()
//}