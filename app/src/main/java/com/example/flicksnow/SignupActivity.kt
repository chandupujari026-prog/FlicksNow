package com.example.flicksnow

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.flicksnow.ui.theme.FlicksNowTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlicksNowTheme {
                SignupScreen(
                    onSignupSuccess = {
                        // For now: go back to Login screen
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    },
                    onNavigateToLogin = {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val focus = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var fullName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showConfirmPassword by rememberSaveable { mutableStateOf(false) }

    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var confirmPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    var isLoading by rememberSaveable { mutableStateOf(false) }

    fun validate(): Boolean {
        nameError = if (fullName.isBlank()) "Name is required" else null

        emailError = when {
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Enter a valid email"
            else -> null
        }

        passwordError = when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Minimum 6 characters"
            else -> null
        }

        confirmPasswordError = when {
            confirmPassword.isBlank() -> "Please confirm password"
            confirmPassword != password -> "Passwords do not match"
            else -> null
        }

        return nameError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null
    }

    // TODO: Replace with real Firebase sign-up later
    suspend fun fakeSignUp(
        name: String,
        email: String,
        password: String
    ): Boolean {
        delay(1200)
        return true // always succeeds for now
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFDFBFB),
            Color(0xFFECEFF1),
            Color(0xFFD6EAF8)
        )
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))

                Text(
                    text = "Create your FlicksNow account",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF212121)
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        if (nameError != null) nameError = null
                    },
                    label = { Text("Full name") },
                    singleLine = true,
                    isError = nameError != null,
                    supportingText = { if (nameError != null) Text(nameError!!) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (emailError != null) emailError = null
                    },
                    label = { Text("Email") },
                    singleLine = true,
                    isError = emailError != null,
                    supportingText = { if (emailError != null) Text(emailError!!) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (passwordError != null) passwordError = null
                    },
                    label = { Text("Password") },
                    singleLine = true,
                    isError = passwordError != null,
                    supportingText = { if (passwordError != null) Text(password!!) },
                    visualTransformation = if (showPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        Text(
                            if (showPassword) "HIDE" else "SHOW",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { showPassword = !showPassword }
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        if (confirmPasswordError != null) confirmPasswordError = null
                    },
                    label = { Text("Confirm password") },
                    singleLine = true,
                    isError = confirmPasswordError != null,
                    supportingText = { if (confirmPasswordError != null) Text(confirmPasswordError!!) },
                    visualTransformation = if (showConfirmPassword) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        Text(
                            if (showConfirmPassword) "HIDE" else "SHOW",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { showConfirmPassword = !showConfirmPassword }
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        focus.clearFocus()
                        if (!isLoading && validate()) {
                            isLoading = true
                            scope.launch {
                                val ok = fakeSignUp(
                                    fullName.trim(),
                                    email.trim(),
                                    password
                                )
                                isLoading = false
                                if (ok) {
                                    snackbarHostState.showSnackbar("Account created! Please sign in.")
                                    onSignupSuccess()
                                } else {
                                    snackbarHostState.showSnackbar("Sign up failed. Try again.")
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text("Create account")
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account? ")
                    Text(
                        text = "Sign in",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
