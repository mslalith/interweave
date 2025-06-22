package dev.mslalith.interweave.ui.shimmer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepo(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("owner")
    val owner: GithubRepoOwner,
    @SerialName("html_url")
    val url: String,
    @SerialName("description")
    val description: String?
) {
    companion object {
        fun empty(id: Long) = GithubRepo(
            id = id,
            name = "",
            owner = GithubRepoOwner.EMPTY,
            url = "",
            description = null
        )
    }
}

@Serializable
data class GithubRepoOwner(
    @SerialName("id")
    val id: Long,
    @SerialName("login")
    val name: String
) {
    companion object {
        val EMPTY = GithubRepoOwner(
            id = 0,
            name = ""
        )
    }
}
