package com.nammayantra.app.ui.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nammayantra.app.R
import com.nammayantra.app.data.model.UserProfile
import com.nammayantra.app.ui.components.hardShadow
import com.nammayantra.app.ui.theme.*

@Composable
fun AuthScreen(onLoginSuccess: (UserProfile) -> Unit) {
    var isSignUp by remember { mutableStateOf(false) }

    if (isSignUp) {
        SignUpScreen(
            onSignedUp = onLoginSuccess,
            onBackToLogin = { isSignUp = false }
        )
    } else {
        LoginScreen(
            onLoggedIn = onLoginSuccess,
            onGoToSignUp = { isSignUp = true }
        )
    }
}

@Composable
private fun LoginScreen(onLoggedIn: (UserProfile) -> Unit, onGoToSignUp: () -> Unit) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { viewModel.signInWithGoogle(it) }
            } catch (e: ApiException) {
                viewModel.handleGoogleError("Google sign in failed: ${e.statusCode}")
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthViewModel.UiState.Success) onLoggedIn((uiState as AuthViewModel.UiState.Success).profile)
    }

    val isLoading = uiState is AuthViewModel.UiState.Loading
    val error = (uiState as? AuthViewModel.UiState.Error)?.message

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Icon(
            imageVector = Icons.Filled.Agriculture,
            contentDescription = "KrishiYantra",
            tint = ForestGreen,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "KRISHIYANTRA",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ForestGreen,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Reliable Machinery Rental",
            fontSize = 16.sp,
            color = NammaOnSurfaceVariant,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Form card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .hardShadow()
                .border(2.dp, InkBlack)
                .background(PureWhite)
                .padding(20.dp)
        ) {
            Column {
                KrishiLabel("EMAIL ADDRESS")
                Spacer(modifier = Modifier.height(6.dp))
                KrishiTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Enter your email"
                )
                Spacer(modifier = Modifier.height(20.dp))
                KrishiLabel("PASSWORD")
                Spacer(modifier = Modifier.height(6.dp))
                KrishiTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Enter your password",
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = null,
                                tint = NammaOnSurfaceVariant
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(28.dp))

                error?.let {
                    Text(it, color = ErrorRed, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))
                }

                // Orange login button
                Button(
                    onClick = { viewModel.signIn(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .hardShadow(EarthOrange),
                    colors = ButtonDefaults.buttonColors(containerColor = EarthOrange),
                    shape = MaterialTheme.shapes.extraSmall,
                    enabled = email.isNotBlank() && password.length >= 6 && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = PureWhite, modifier = Modifier.size(24.dp))
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Text("LOGIN", fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                KrishiDivider()
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(context.getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build()
                        val client = GoogleSignIn.getClient(context, gso)
                        googleLauncher.launch(client.signInIntent)
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, InkBlack),
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = InkBlack),
                    enabled = !isLoading
                ) {
                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SIGN IN WITH GOOGLE", fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 0.5.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Text("Don't have an account? ", color = InkBlack)
            TextButton(onClick = { viewModel.clearError(); onGoToSignUp() }, contentPadding = PaddingValues(0.dp)) {
                Text("Sign up", color = ForestGreen, fontWeight = FontWeight.Bold,
                    style = LocalTextStyle.current.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline))
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun SignUpScreen(onSignedUp: (UserProfile) -> Unit, onBackToLogin: () -> Unit) {
    val viewModel: AuthViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Farmer") }
    val context = LocalContext.current

    val googleLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { viewModel.signInWithGoogle(it) }
            } catch (e: ApiException) {
                viewModel.handleGoogleError("Google sign up failed: ${e.statusCode}")
            }
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthViewModel.UiState.Success) onSignedUp((uiState as AuthViewModel.UiState.Success).profile)
    }

    val isLoading = uiState is AuthViewModel.UiState.Loading
    val error = (uiState as? AuthViewModel.UiState.Error)?.message

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        IconButton(onClick = { viewModel.clearError(); onBackToLogin() }, modifier = Modifier.padding(start = 8.dp)) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = InkBlack)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Create Account", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = InkBlack)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Register to access heavy machinery and field services.", fontSize = 15.sp, color = NammaOnSurfaceVariant)
            Spacer(modifier = Modifier.height(32.dp))

            KrishiLabel("FULL NAME")
            Spacer(modifier = Modifier.height(6.dp))
            KrishiTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = "Enter your full name",
                leadingIcon = { Icon(Icons.Filled.Person, null, tint = NammaOnSurfaceVariant) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            KrishiLabel("EMAIL ADDRESS")
            Spacer(modifier = Modifier.height(6.dp))
            KrishiTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Enter your email",
                leadingIcon = { Icon(Icons.Filled.Email, null, tint = NammaOnSurfaceVariant) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            KrishiLabel("PASSWORD")
            Spacer(modifier = Modifier.height(6.dp))
            KrishiTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Create a password",
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(Icons.Filled.Lock, null, tint = NammaOnSurfaceVariant) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            null, tint = NammaOnSurfaceVariant
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(28.dp))

            error?.let {
                Text(it, color = ErrorRed, fontSize = 13.sp, modifier = Modifier.padding(bottom = 12.dp))
            }

            Button(
                onClick = { viewModel.signUp(email, password, name, selectedRole) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .hardShadow(ForestGreen),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = MaterialTheme.shapes.extraSmall,
                enabled = name.isNotBlank() && email.isNotBlank() && password.length >= 6 && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = PureWhite, modifier = Modifier.size(24.dp))
                } else {
                    Text("CREATE ACCOUNT", fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 1.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            KrishiDivider()
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    val client = GoogleSignIn.getClient(context, gso)
                    googleLauncher.launch(client.signInIntent)
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, ForestGreen),
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen),
                enabled = !isLoading
            ) {
                Text("Sign up with Google", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have an account? ", color = InkBlack, fontSize = 14.sp)
                Text(
                    "Login",
                    color = ForestGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    style = LocalTextStyle.current.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline),
                    modifier = Modifier.clickable { viewModel.clearError(); onBackToLogin() }
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun KrishiLabel(text: String) {
    Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkBlack, letterSpacing = 0.5.sp)
}

@Composable
fun KrishiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = NammaOnSurfaceVariant) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = MaterialTheme.shapes.extraSmall,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = InkBlack,
            focusedBorderColor = ForestGreen,
            unfocusedContainerColor = SurfaceContainerLow,
            focusedContainerColor = PureWhite
        )
    )
}

@Composable
fun KrishiDivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f), color = NammaOutlineVariant)
        Text("  OR  ", fontSize = 13.sp, color = NammaOnSurfaceVariant)
        Divider(modifier = Modifier.weight(1f), color = NammaOutlineVariant)
    }
}
