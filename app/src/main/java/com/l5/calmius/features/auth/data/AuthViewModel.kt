package com.l5.calmius.features.auth.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    //    private val context = application.applicationContext
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestoreHelper = UserRepository()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _currentUser = MutableLiveData<UserData?>()
    val currentUser: LiveData<UserData?> = _currentUser

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val firebaseUser = auth.currentUser
        if (firebaseUser == null) {
            _authState.value = AuthState.Unauthenticated
            _currentUser.value = null
            println("Auth status: Unauthenticated")
        } else {
            firestoreHelper.getUser(firebaseUser.uid, { userData ->
                _currentUser.value = UserData(
                    userId = userData["id"] as String,
                    username = userData["name"] as String? ?: "",
                    phoneNumber = userData["phone"] as String? ?: "",
                    email = userData["email"] as String? ?: "",
                    password = "",
                    profilePictureUrl = firebaseUser.photoUrl?.toString() ?: ""
                )
                _authState.value = AuthState.Authenticated
                println("Auth status: Authenticated, user: ${firebaseUser.displayName}")
            }, { error ->
                _authState.value = AuthState.Error(error)
                println("Auth status: Error, reason: $error")
            })
        }
    }


    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkAuthStatus()
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun signUp(fullName: String, phoneNum: String, email: String, password: String) {
        if (fullName.isEmpty() || phoneNum.isEmpty() || email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Fields must not be empty")
            return
        }

        _authState.value = AuthState.Loading

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName(fullName)
                            .build()

                        firebaseUser.updateProfile(profileUpdate).addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                // Simpan data ke Firestore
                                firestoreHelper.saveUser(
                                    userId = firebaseUser.uid,
                                    fullName = fullName,
                                    phoneNum = phoneNum,
                                    email = email,
                                    onSuccess = {
                                        _authState.value = AuthState.SignupSuccess
                                    },
                                    onFailure = { error ->
                                        _authState.value = AuthState.Error(error)
                                    }
                                )
                            } else {
                                _authState.value = AuthState.Error(profileTask.exception?.message ?: "Failed to update profile")
                            }
                        }
                    } else {
                        _authState.value = AuthState.Error("User creation failed")
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Unauthenticated
    }

    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onSignInResult(result: AuthenticatedResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { State() }
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object SignupSuccess : AuthState()
    data class Error(val message: String) : AuthState()
}
