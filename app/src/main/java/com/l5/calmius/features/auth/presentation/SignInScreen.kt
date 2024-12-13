package com.l5.calmius.features.auth.presentation

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import com.l5.calmius.R
import com.l5.calmius.features.auth.data.AuthState
import com.l5.calmius.features.auth.data.AuthViewModel
import com.l5.calmius.features.auth.data.State

@Composable
fun SignInScreen(
    navController: NavController, authViewModel: AuthViewModel, state: State, googleSignIn: () -> Unit
) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate("home") {
                    popUpTo("signIn") { inclusive = true }
                }
            }
            else -> {}
        }
    }


    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
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
                    text = "Sign In",
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
                Spacer(modifier = Modifier.height(20.dp))
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
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Forgot your password?",
                    fontSize = 15.sp,
                    color = Color(0xff65849C),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(6.dp)
                )
                Spacer(modifier = Modifier.height(35.dp))
                Button(
                    onClick = {
                        authViewModel.signIn(email,password)
                    },
                    enabled = authState.value != AuthState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xff7397B3))
                ) {
                    Text("Sign In")
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account?",
                        color = Color.Gray,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.SansSerif
                    )
                    TextButton(
                        onClick = {
                            navController.navigate("signUp")

                        },
                        contentPadding = PaddingValues(start = 2.dp)
                    ) {
                        Text(
                            text = "Sign Up",
                            color = Color(0xff65849C),
                            fontSize = 15.sp,
                            fontFamily = FontFamily.SansSerif,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Link an account to log in faster in the future",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(30.dp))
                TransparentLoginButton(
                    text = "Continue with Google",
                    icon = R.drawable.google,
                    borderColor = Color(0xff65849C),
                    onClick = {
                        googleSignIn()
                    }
                )
                Spacer(modifier = Modifier.height(15.dp))
                TransparentLoginButton(
                    text = "Continue with Facebook",
                    icon = R.drawable.facebook,
                    borderColor = Color(0xff65849C),
                    onClick = { /*TODO*/ }
                )
                Spacer(modifier = Modifier.height(30.dp))
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
fun TransparentLoginButton(
    text: String,
    icon: Int,
    borderColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        border = BorderStroke(1.dp, borderColor),
        shape = RoundedCornerShape(24.dp)
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Alternative",
            modifier = Modifier
                .size(30.dp)
                .padding(end = 8.dp),
            tint = Color.Unspecified
        )
        Text(
            text = text,
            color = Color(0xff49454F).copy(alpha = 0.9f)
        )

    }
}

//@Preview(showBackground = true)
//@Composable
//fun SignInPagePreview() {
//    SignInPage()
//}