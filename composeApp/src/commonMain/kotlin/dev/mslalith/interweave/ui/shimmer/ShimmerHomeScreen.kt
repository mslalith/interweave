package dev.mslalith.interweave.ui.shimmer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mslalith.interweave.core.navigation.LocalNavigator
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShimmerHomeScreenContent(
    viewModel: ShimmerHomeScreenViewModel = koinViewModel()
) {
    val navigator = LocalNavigator.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Shimmer") },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.pop() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(paddingValues = padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = state.searchQuery,
                onValueChange = viewModel::onQueryChange
            )
            LazyColumn(
                modifier = Modifier.weight(weight = 1f)
            ) {
                items(
                    items = state.repos,
                    key = { it.id }
                ) {
                    ListItem(
                        headlineContent = { Text(it.name) },
                        supportingContent = { Text(it.description ?: "") }
                    )
                }
            }
        }
    }
}