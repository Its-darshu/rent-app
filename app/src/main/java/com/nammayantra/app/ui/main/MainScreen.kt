package com.nammayantra.app.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammayantra.app.data.model.Equipment
import com.nammayantra.app.data.model.UserProfile
import com.nammayantra.app.ui.listing.AddListingScreen
import com.nammayantra.app.ui.listing.ListingDetailScreen
import com.nammayantra.app.ui.marketplace.MarketplaceHomeScreen
import com.nammayantra.app.ui.mylistings.MyListingsScreen
import com.nammayantra.app.ui.profile.ProfileScreen
import com.nammayantra.app.ui.theme.*

private sealed class MainNav {
    object Home : MainNav()
    data class Detail(val equipment: Equipment) : MainNav()
    object AddListing : MainNav()
}

private data class NavItem(val label: String, val icon: ImageVector)

private val navItems = listOf(
    NavItem("Home", Icons.Filled.GridView),
    NavItem("My Listings", Icons.Filled.List),
    NavItem("Profile", Icons.Filled.Person)
)

@Composable
fun MainScreen(profile: UserProfile, onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    var nav by remember { mutableStateOf<MainNav>(MainNav.Home) }

    // Full-screen overlays (detail, add listing)
    when (val current = nav) {
        is MainNav.Detail -> {
            ListingDetailScreen(
                equipment = current.equipment,
                onBack = { nav = MainNav.Home }
            )
            return
        }
        is MainNav.AddListing -> {
            AddListingScreen(
                profile = profile,
                onBack = { nav = MainNav.Home },
                onSuccess = { nav = MainNav.Home; selectedTab = 1 }
            )
            return
        }
        else -> {}
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForestGreen)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.Agriculture, null, tint = PureWhite, modifier = Modifier.size(22.dp))
                    Text("KRISHIYANTRA", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = PureWhite, letterSpacing = 0.5.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, null, tint = PureWhite.copy(alpha = 0.8f), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("India", fontSize = 13.sp, color = PureWhite.copy(alpha = 0.8f))
                }
            }
        },
        floatingActionButton = {
            if (selectedTab == 0 || selectedTab == 1) {
                FloatingActionButton(
                    onClick = { nav = MainNav.AddListing },
                    containerColor = EarthOrange,
                    contentColor = PureWhite
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Post Listing", modifier = Modifier.size(28.dp))
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = PureWhite,
                tonalElevation = 0.dp,
                modifier = Modifier.border(width = 1.5.dp, color = NammaOutlineVariant)
            ) {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(item.icon, contentDescription = item.label, modifier = Modifier.size(22.dp)) },
                        label = {
                            Text(
                                item.label,
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PureWhite,
                            unselectedIconColor = NammaOnSurfaceVariant,
                            selectedTextColor = EarthOrange,
                            unselectedTextColor = NammaOnSurfaceVariant,
                            indicatorColor = EarthOrange
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (selectedTab) {
                0 -> MarketplaceHomeScreen(onListingClick = { nav = MainNav.Detail(it) })
                1 -> MyListingsScreen(ownerId = profile.id)
                2 -> ProfileScreen(userName = profile.name, userRole = profile.role, onLogout = onLogout)
            }
        }
    }
}
