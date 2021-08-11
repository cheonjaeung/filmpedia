package io.woong.filmpedia.data

import com.google.gson.annotations.SerializedName

data class ExternalIds(
    val id: Int,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("facebook_id") val facebookId: String?,
    @SerializedName("instagram_id") val instagramId: String?,
    @SerializedName("twitter_id") val twitterId: String?
)
