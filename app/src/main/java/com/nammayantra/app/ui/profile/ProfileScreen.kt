package com.nammayantra.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nammayantra.app.ui.components.hardShadow
import com.nammayantra.app.ui.theme.*

@Composable
fun ProfileScreen(
    userName: String,
    userRole: String,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OffWhite)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(ForestGreenContainer)
                .border(3.dp, InkBlack, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = PureWhite
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(userName.ifBlank { "User" }, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = InkBlack)

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .border(2.dp, ForestGreen)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(userRole.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold,
                color = ForestGreen, letterSpacing = 1.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        ProfileOption(icon = Icons.Default.LocationOn, title = "My Addresses", subtitle = "Manage saved locations")
        ProfileOption(icon = Icons.Default.Settings, title = "Settings", subtitle = "App preferences and notifications")
        ProfileOption(icon = Icons.Default.HelpOutline, title = "Help & Support", subtitle = "FAQs and contact us")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .hardShadow(ErrorRed),
            colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Icon(Icons.Filled.Logout, null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("LOGOUT", fontWeight = FontWeight.Bold, fontSize = 15.sp, letterSpacing = 1.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileOption(icon: ImageVector, title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(2.dp, NammaOutlineVariant)
            .background(PureWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = ForestGreen, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = InkBlack)
                Text(subtitle, fontSize = 13.sp, color = NammaOnSurfaceVariant)
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Filled.ChevronRight, null, tint = NammaOnSurfaceVariant)
        }
    }
}
