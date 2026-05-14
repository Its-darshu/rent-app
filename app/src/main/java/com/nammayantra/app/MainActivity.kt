package com.nammayantra.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nammayantra.app.data.model.UserProfile
import com.nammayantra.app.data.repository.AuthRepository
import com.nammayantra.app.ui.auth.AuthScreen
import com.nammayantra.app.ui.main.MainScreen
import com.nammayantra.app.ui.theme.ForestGreen
import com.nammayantra.app.ui.theme.NammaYantraTheme
import kotlinx.coroutines.launch

sealed class AppState {
    object Loading : AppState()
    object LoggedOut : AppState()
    data class LoggedIn(val profile: UserProfile) : AppState()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaYantraTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var appState by remember { mutableStateOf<AppState>(AppState.Loading) }
                    val coroutineScope = rememberCoroutineScope()

                    LaunchedEffect(Unit) {
                        val profile = AuthRepository.currentProfile()
                        appState = if (profile != null) AppState.LoggedIn(profile) else AppState.LoggedOut
                    }

                    when (val state = appState) {
                        is AppState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = ForestGreen)
                            }
                        }
                        is AppState.LoggedOut -> {
                            AuthScreen(onLoginSuccess = { profile ->
                                appState = AppState.LoggedIn(profile)
                            })
                        }
                        is AppState.LoggedIn -> {
                            MainScreen(
                                profile = state.profile,
                                onLogout = {
                                    coroutineScope.launch { AuthRepository.signOut() }
                                    appState = AppState.LoggedOut
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
