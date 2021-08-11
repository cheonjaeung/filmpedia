package io.woong.filmpedia.util

/**
 * TMDB API's image path is uncompleted.
 * This util class helps to convert to full url.
 */
object ImagePathUtil {

    private const val BASE_URL: String = "https://image.tmdb.org/t/p/original"

    fun toFullUrl(path: String?): String? {
        path?.let {
            return BASE_URL + path
        } ?: return null
    }
}
