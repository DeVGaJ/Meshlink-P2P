package com.devgaj.meshlink.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * Developed by DeVGaJ - https://github.com/DeVGaJ
 * Nightshade Theme UI for MeshLink.
 */

val VibrantPurple = Color(0xFFA020F0)
val TrueBlack = Color(0xFF000000)
val LightGrey = Color(0xFFE0E0E0)
val DarkGrey = Color(0xFF1A1A1A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeshChatScreen(
    userName: String,
    messages: MutableList<String>,
    status: String = "Disconnected",
    onSendMessage: (String) -> Unit
) {
    var textState by remember { mutableStateOf("") }
    var showAbout by remember { mutableStateOf(false) }

    if (showAbout) {
        AboutDialog(onDismiss = { showAbout = false })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("MeshLink", color = VibrantPurple, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(status, color = if (status == "Connected") Color.Green else Color.Gray, fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TrueBlack),
                actions = {
                    IconButton(onClick = { showAbout = true }) {
                        Icon(Icons.Default.Info, contentDescription = "About", tint = VibrantPurple)
                    }
                }
            )
        },
        containerColor = TrueBlack
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                reverseLayout = false
            ) {
                items(messages) { msg ->
                    MessageBubble(msg)
                }
            }

            ChatInputArea(
                textState = textState,
                onTextChange = { textState = it },
                onSend = {
                    if (textState.isNotBlank()) {
                        messages.add("Me: $textState")
                        onSendMessage(textState)
                        textState = ""
                    }
                }
            )
        }
    }
}

@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkGrey)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MeshLink",
                    style = MaterialTheme.typography.headlineMedium,
                    color = VibrantPurple,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Developed by DeVGaJ",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "GitHub: https://github.com/DeVGaJ",
                    color = VibrantPurple,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = VibrantPurple)
                ) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun MessageBubble(text: String) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .background(DarkGrey, RoundedCornerShape(12.dp))
            .border(0.5.dp, VibrantPurple.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = LightGrey, fontSize = 14.sp)
    }
}

@Composable
fun ChatInputArea(textState: String, onTextChange: (String) -> Unit, onSend: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = textState,
            onValueChange = onTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Encrypted message...", color = Color.Gray) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF0A0A0A),
                unfocusedContainerColor = Color(0xFF0A0A0A),
                focusedIndicatorColor = VibrantPurple,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = VibrantPurple
            ),
            shape = RoundedCornerShape(25.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(
            onClick = onSend,
            modifier = Modifier
                .background(VibrantPurple, CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
        }
    }
}
