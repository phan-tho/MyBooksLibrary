package com.example.mybookslibrary.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DiscoverScreen() {
    CenteredText("Discover")
}

@Composable
fun SearchScreen() {
    CenteredText("Search")
}

@Composable
fun LibraryScreen() {
    CenteredText("My Library")
}

@Composable
fun SettingScreen() {
    CenteredText("Setting")
}

@Composable
private fun CenteredText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

