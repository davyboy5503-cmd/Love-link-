package com.lovelink.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class BubbleMessage(val text: String, val mine: Boolean)
private data class Palette(val name: String, val accent: Color, val sent: Color, val received: Color, val background: Color)

private val palettes = listOf(
    Palette("Violet", Color(0xFFB46CFF), Color(0xFF512080), Color(0xFF292238), Color(0xFF0D0A14)),
    Palette("Electric blue", Color(0xFF35B9FF), Color(0xFF124E78), Color(0xFF1D2B3A), Color(0xFF07111A)),
    Palette("Rose neon", Color(0xFFFF65B5), Color(0xFF852653), Color(0xFF382131), Color(0xFF160A12)),
    Palette("Emerald", Color(0xFF42E6B0), Color(0xFF17634F), Color(0xFF20352F), Color(0xFF07120F)),
    Palette("Solar", Color(0xFFFFB347), Color(0xFF80501D), Color(0xFF393027), Color(0xFF130F09))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LoveLinkApp() }
    }
}

@Composable
private fun LoveLinkApp() {
    var page by remember { mutableStateOf("Chat") }
    var palette by remember { mutableStateOf(palettes.first()) }
    var bubbleStyle by remember { mutableStateOf("Rounded") }
    var draft by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf(
        BubbleMessage("Hey love ❤️", false),
        BubbleMessage("I was just thinking about you ✨", true),
        BubbleMessage("You better be 😂", false)
    ) }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = palette.accent,
        background = palette.background,
        surface = Color(0xFF171321),
        onBackground = Color.White,
        onSurface = Color.White
    )) {
        Scaffold(
            containerColor = palette.background,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF171321), contentColor = Color.White) {
                    listOf("Chat", "Memories", "Together", "Settings").forEach { item ->
                        val icon = when (item) {
                            "Chat" -> Icons.Default.ChatBubble
                            "Memories" -> Icons.Default.Collections
                            "Together" -> Icons.Default.Favorite
                            else -> Icons.Default.Settings
                        }
                        NavigationBarItem(
                            selected = page == item,
                            onClick = { page = item },
                            icon = { Icon(icon, contentDescription = item) },
                            label = { Text(item) },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = palette.accent, indicatorColor = palette.accent.copy(alpha = .15f), selectedTextColor = palette.accent)
                        )
                    }
                }
            }
        ) { padding ->
            when (page) {
                "Chat" -> ChatScreen(padding, palette, bubbleStyle, messages, draft, { draft = it }) {
                    if (draft.isNotBlank()) { messages.add(BubbleMessage(draft.trim(), true)); draft = "" }
                }
                "Settings" -> SettingsScreen(padding, palette, bubbleStyle, onPalette = { palette = it }, onStyle = { bubbleStyle = it })
                "Memories" -> PlaceholderScreen(padding, "Shared memories", "A private space for photos and moments you save together.", Icons.Default.Collections)
                else -> PlaceholderScreen(padding, "Your together space", "Countdowns, daily questions and little reminders will live here.", Icons.Default.Favorite)
            }
        }
    }
}

@Composable
private fun ChatScreen(
    padding: PaddingValues, palette: Palette, bubbleStyle: String,
    messages: List<BubbleMessage>, draft: String, onDraft: (String) -> Unit, onSend: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
        Row(Modifier.fillMaxWidth().padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(42.dp).clip(CircleShape).background(palette.accent.copy(alpha = .18f)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Favorite, null, tint = palette.accent)
            }
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text("LoveLink", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Your private space", fontSize = 12.sp, color = Color.LightGray)
            }
            Icon(Icons.Default.MoreVert, null, tint = Color.White)
        }
        Text("TODAY", Modifier.align(Alignment.CenterHorizontally).padding(vertical = 10.dp), color = Color.Gray, fontSize = 10.sp, letterSpacing = 2.sp)
        LazyColumn(Modifier.weight(1f).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(messages) { message ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = if (message.mine) Arrangement.End else Arrangement.Start) {
                    val shape = when (bubbleStyle) {
                        "Pill" -> RoundedCornerShape(28.dp)
                        "Sharp" -> RoundedCornerShape(5.dp)
                        "Notched" -> RoundedCornerShape(18.dp, 18.dp, if (message.mine) 4.dp else 18.dp, if (message.mine) 18.dp else 4.dp)
                        else -> RoundedCornerShape(20.dp)
                    }
                    Box(
                        Modifier.widthIn(max = 290.dp)
                            .clip(shape)
                            .background(if (message.mine) palette.sent else palette.received)
                            .then(if (message.mine) Modifier.border(1.dp, palette.accent.copy(alpha = .65f), shape) else Modifier)
                            .padding(horizontal = 15.dp, vertical = 11.dp)
                    ) { Text(message.text, color = Color.White, fontSize = 15.sp) }
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {}, enabled = false) { Icon(Icons.Default.Add, null, tint = palette.accent) }
            Row(Modifier.weight(1f).clip(RoundedCornerShape(24.dp)).background(Color(0xFF211B2B)).padding(horizontal = 15.dp, vertical = 13.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.weight(1f)) {
                    if (draft.isEmpty()) Text("Message your person…", color = Color.Gray)
                    BasicTextField(value = draft, onValueChange = onDraft, singleLine = true, textStyle = LocalTextStyle.current.copy(color = Color.White))
                }
                Icon(Icons.Default.Mic, null, tint = palette.accent)
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onSend, modifier = Modifier.clip(CircleShape).background(palette.accent)) {
                Icon(Icons.Default.Send, null, tint = Color.White)
            }
        }
        Text("Prototype preview · Messages stay on this device", Modifier.align(Alignment.CenterHorizontally).padding(bottom = 6.dp), color = Color.Gray, fontSize = 10.sp)
    }
}

@Composable
private fun SettingsScreen(padding: PaddingValues, palette: Palette, bubbleStyle: String, onPalette: (Palette) -> Unit, onStyle: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
        Text("Appearance", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Make your LoveLink feel like yours.", color = Color.LightGray, modifier = Modifier.padding(top = 4.dp, bottom = 22.dp))
        Text("NEON NIGHT COLOURS", color = palette.accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        Text("Choose an accent palette", color = Color.White, modifier = Modifier.padding(top = 10.dp, bottom = 12.dp))
        palettes.forEach { option ->
            Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable { onPalette(option) }.background(if (palette.name == option.name) Color(0xFF292236) else Color.Transparent).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(30.dp).clip(CircleShape).background(option.accent))
                Text(option.name, Modifier.weight(1f).padding(start = 12.dp), color = Color.White)
                if (palette.name == option.name) Icon(Icons.Default.CheckCircle, null, tint = option.accent)
            }
        }
        Spacer(Modifier.height(22.dp))
        Text("CHAT BUBBLE SHAPE", color = palette.accent, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
        listOf("Rounded", "Pill", "Sharp", "Notched").forEach { style ->
            Row(Modifier.fillMaxWidth().clickable { onStyle(style) }.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(style, Modifier.weight(1f), color = Color.White)
                RadioButton(selected = bubbleStyle == style, onClick = { onStyle(style) }, colors = RadioButtonDefaults.colors(selectedColor = palette.accent))
            }
        }
        Text("More controls planned: custom hex colours, independent sent/received colours, wallpapers, gradients, glow strength and saved themes.", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 12.dp))
    }
}

@Composable
private fun PlaceholderScreen(padding: PaddingValues, title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, null, tint = Color(0xFFB46CFF), modifier = Modifier.size(54.dp))
        Text(title, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
        Text(description, color = Color.LightGray, modifier = Modifier.padding(top = 8.dp))
        Text("Coming in a later development stage", color = Color(0xFFB46CFF), fontSize = 12.sp, modifier = Modifier.padding(top = 18.dp))
    }
}
