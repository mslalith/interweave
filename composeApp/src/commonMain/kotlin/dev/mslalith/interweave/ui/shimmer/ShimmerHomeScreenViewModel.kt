package dev.mslalith.interweave.ui.shimmer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTimedValue

data class ShimmerHomeScreenState(
    val repos: List<GithubRepo> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val apiDurationMs: Float = 2000f
)

@KoinViewModel
class ShimmerHomeScreenViewModel(
    private val httpClient: HttpClient
) : ViewModel() {

    private val _state = MutableStateFlow(ShimmerHomeScreenState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    fun updateApiDuration(durationMs: Float) {
        _state.update { it.copy(apiDurationMs = durationMs) }
    }

    fun onQueryChange(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, searchQuery = query) }
            val apiDuration = _state.value.apiDurationMs.toInt()
            val repos = runForAtleast(apiDuration.milliseconds) {
                fetchRepos(query = query)
            }
            _state.update { it.copy(isLoading = false, repos = repos) }
        }
        searchJob?.invokeOnCompletion { searchJob = null }
    }

    private suspend fun fetchRepos(query: String): List<GithubRepo> {
        if (query.isEmpty()) return emptyList()

        return httpClient
            .get(urlString = "https://api.github.com/search/repositories?q=$query")
            .body<FetchGithubRepoResponse>()
            .items
    }
}

private suspend fun <T> runForAtleast(
    duration: Duration,
    block: suspend () -> T
): T {
    val (result, timeTook) = measureTimedValue { block() }
    if (timeTook < duration) {
        val delayTime = duration - timeTook
        delay(timeMillis = delayTime.inWholeMilliseconds)
    }
    return result
}

@Serializable
private data class FetchGithubRepoResponse(
    @SerialName("items")
    val items: List<GithubRepo>
)