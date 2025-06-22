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
    val description: String?,
    @SerialName("stargazers_count")
    val stars: Int,
    @SerialName("forks_count")
    val forks: Int,
    @SerialName("license")
    val license: GithubRepoLicense?
) {
    companion object {
        fun empty(id: Long) = GithubRepo(
            id = id,
            name = "",
            owner = GithubRepoOwner.EMPTY,
            url = "",
            description = null,
            stars = 0,
            forks = 0,
            license = null
        )
    }
}

@Serializable
data class GithubRepoOwner(
    @SerialName("id")
    val id: Long,
    @SerialName("login")
    val name: String,
    @SerialName("avatar_url")
    val avatarUrl: String
) {
    companion object {
        val EMPTY = GithubRepoOwner(
            id = 0,
            name = "",
            avatarUrl = ""
        )
    }
}

@Serializable
data class GithubRepoLicense(
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("spdx_id")
    val shortName: String
) {
    companion object {
        val EMPTY = GithubRepoLicense(
            key = "",
            name = "",
            shortName = ""
        )
    }
}
