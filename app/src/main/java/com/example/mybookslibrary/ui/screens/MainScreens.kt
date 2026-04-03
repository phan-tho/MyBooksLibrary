package com.example.mybookslibrary.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mybookslibrary.data.local.AppDatabase
import com.example.mybookslibrary.data.local.LibraryStatus
import com.example.mybookslibrary.data.repository.LibraryRepository
import com.example.mybookslibrary.ui.viewmodel.LibraryViewModel
import com.example.mybookslibrary.ui.viewmodel.LibraryViewModelFactory

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
    val context = LocalContext.current

    // Skeleton: tạo database + repository local-first trực tiếp (chưa dùng DI/Hilt).
    val database = androidx.compose.runtime.remember(context) { AppDatabase.getInstance(context) }
    val repository = androidx.compose.runtime.remember(database) { LibraryRepository(database.libraryDao()) }
    val factory = androidx.compose.runtime.remember(repository) { LibraryViewModelFactory(repository) }

    val vm: LibraryViewModel = viewModel(factory = factory)
    val items by vm.libraryItems.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        items(items, key = { it.manga_id }) { item ->
            Text(text = "${item.title} - ${item.status.toDisplayName()}")
        }
    }
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

private fun LibraryStatus.toDisplayName(): String = when (this) {
    LibraryStatus.READING -> "Đang đọc"
    LibraryStatus.COMPLETED -> "Đã đọc"
    LibraryStatus.FAVORITE -> "Yêu thích"
}

