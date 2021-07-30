package io.woong.filmpedia.data

import com.google.gson.annotations.SerializedName

/**
 * A data class for containing TMDB API's error response.
 */
data class ErrorResponse(
    @SerializedName("status_message") val statusMessage: String,
    @SerializedName("status_code") val statusCode: Int
)
