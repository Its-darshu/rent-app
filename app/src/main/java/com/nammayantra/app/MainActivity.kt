package com.nammayantra.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nammayantra.app.ui.auth.AuthScreen
import com.nammayantra.app.ui.home.HomeScreen
import com.nammayantra.app.ui.booking.BookingScreen
import com.nammayantra.app.ui.requests.RequestsScreen
import com.nammayantra.app.ui.profile.ProfileScreen
import com.nammayantra.app.data.model.Equipment
import com.nammayantra.app.ui.theme.NammaYantraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NammaYantraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(false) }
                    var userRole by remember { mutableStateOf("") }
                    var selectedEquipment by remember { mutableStateOf<Equipment?>(null) }
                    var showRequests by remember { mutableStateOf(false) }
                    var showProfile by remember { mutableStateOf(false) }

                    if (!isLoggedIn) {
                        AuthScreen(
                            onLoginSuccess = { role ->
                                userRole = role
                                isLoggedIn = true
                            }
                        )
                    } else if (showProfile) {
                        ProfileScreen(
                            userRole = userRole,
                            onBack = { showProfile = false },
                            onLogout = {
                                isLoggedIn = false
                                showProfile = false
                                userRole = ""
                            }
                        )
                    } else if (showRequests) {
                        RequestsScreen(
                            userRole = userRole,
                            onBack = { showRequests = false }
                        )
                    } else if (selectedEquipment != null) {
                        BookingScreen(
                            equipment = selectedEquipment!!,
                            onBack = { selectedEquipment = null },
                            onRequestSent = {
                                // TODO: Handle sending request through Supabase Realtime
                                println("Request sent!")
                                selectedEquipment = null // go back to home for now
                                showRequests = true // Immediately show the user their new request
                            }
                        )
                    } else {
                        HomeScreen(
                            userRole = userRole,
                            onEquipmentClick = { equipment ->
                                selectedEquipment = equipment
                            },
                            onViewRequests = { showRequests = true },
                            onProfileClick = { showProfile = true }
                        )
                    }
                }
            }
        }
    }
}

// Removing Greeting since we have HomeScreen now