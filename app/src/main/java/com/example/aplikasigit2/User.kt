package com.example.aplikasigit2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class SearchResponse(
    @SerializedName("total_count")
    var totalCount: Int,

    @SerializedName("incomplete_results")
    var incompleteResults: Boolean,

    var items: List<User>
)

@Parcelize
data class User(
    var login: String,
    var id: String,

    @SerializedName("avatar_url")
    var avatarUrl: String
) : Parcelable

data class DetailUser(
    var login: String,
    var name: String,
    var location: String,

    @SerializedName("public_repos")
    var publicRepos: String,

    var company: String,
    var followers: String,
    var following: String,

    @SerializedName("avatar_url")
    var avatarUrl: String
)


