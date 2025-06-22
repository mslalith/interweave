package dev.mslalith.interweave.ui.shimmer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.valentinilk.shimmer.shimmer
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
            Settings(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                apiDurationMs = { state.apiDurationMs },
                onApiDurationMsChange = viewModel::updateApiDuration
            )
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onQueryChange,
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (state.searchQuery.isEmpty()) {
                Text(
                    text = "Search for a repo",
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
            } else {
                GithubRepoList(
                    repoList = state.repos,
                    isLoading = { state.isLoading }
                )
            }
        }
    }
}

@Composable
private fun Settings(
    modifier: Modifier = Modifier,
    apiDurationMs: () -> Float,
    onApiDurationMsChange: (Float) -> Unit
) {
    val settingsBorderColor = MaterialTheme.colorScheme.primaryContainer
    val settingBackgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)

    var showSettings by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = settingsBorderColor,
                shape = RoundedCornerShape(size = 4.dp)
            )
    ) {
        ListItem(
            modifier = Modifier
                .clickable { showSettings = !showSettings },
            colors = ListItemDefaults.colors(containerColor = settingBackgroundColor),
            headlineContent = @Composable {
                Crossfade(
                    targetState = if (showSettings) "Settings" else "Show Settings",
                    modifier = Modifier.weight(weight = 1f)
                ) { Text(it) }
            },
            trailingContent = @Composable {
                AnimatedVisibility(
                    visible = showSettings
                ) {
                    IconButton(
                        onClick = { showSettings = false }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Settings"
                        )
                    }
                }
            }
        )

        AnimatedVisibility(
            visible = showSettings
        ) {
            Column(
                modifier = Modifier
                    .background(color = settingBackgroundColor)
                    .padding(all = 16.dp)
            ) {
                Row {
                    Text(
                        text = "API Duration in millis",
                        modifier = Modifier.weight(weight = 1f)
                    )
                    Text(text = apiDurationMs().toInt().toString())
                }
                Slider(
                    value = apiDurationMs(),
                    onValueChange = onApiDurationMsChange,
                    valueRange = 0f..5000f,
                    steps = 100
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.GithubRepoList(
    repoList: List<GithubRepo>,
    isLoading: () -> Boolean
) {
    val shimmerList = remember { List(10) { GithubRepo.empty(it.toLong()) } }

    when {
        isLoading() -> {
            LazyColumn(
                modifier = Modifier.weight(weight = 1f)
            ) {
                items(
                    items = shimmerList,
                    key = { it.id }
                ) {
                    GithubRepoItem(
                        githubRepo = it,
                        modifier = Modifier.shimmer(),
                        isLoading = { true }
                    )
                }
            }
        }

        repoList.isEmpty() -> {
            Text(
                text = "No repos found",
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier.weight(weight = 1f)
            ) {
                items(
                    items = repoList,
                    key = { it.id }
                ) {
                    GithubRepoItem(githubRepo = it)
                }
            }
        }
    }
}

@Composable
private fun GithubRepoItem(
    modifier: Modifier = Modifier,
    githubRepo: GithubRepo,
    isLoading: () -> Boolean = { false }
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        ListItem(
            modifier = Modifier
                .clickable(enabled = githubRepo.description != null) {
                    expanded = !expanded
                },
            headlineContent = {
                Box(
                    modifier = Modifier
                        .then(
                            if (!isLoading()) Modifier else Modifier
                                .fillMaxWidth()
                                .height(height = 16.dp)
                                .background(color = Color.DarkGray.copy(alpha = 0.5f))
                        )
                ) { Text(githubRepo.name) }
            },
            supportingContent = {
                Box(
                    modifier = Modifier.then(
                        if (!isLoading()) Modifier else Modifier
                            .fillMaxWidth(fraction = 0.3f)
                            .padding(top = 4.dp)
                            .height(height = 12.dp)
                            .background(color = Color.DarkGray.copy(alpha = 0.5f))
                    )
                ) { Text(githubRepo.owner.name) }
            },
            leadingContent = {
                Box(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(size = 48.dp)
                        .then(
                            if (!isLoading()) Modifier else Modifier
                                .background(color = Color.DarkGray.copy(alpha = 0.5f))
                        )
                ) {
                    AsyncImage(
                        model = githubRepo.owner.avatarUrl,
                        contentDescription = githubRepo.owner.name
                    )
                }
            },
            trailingContent = if (githubRepo.description == null) null else {
                @Composable {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
        )

        AnimatedVisibility(
            visible = expanded,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(githubRepo.description ?: "")
        }
    }
}