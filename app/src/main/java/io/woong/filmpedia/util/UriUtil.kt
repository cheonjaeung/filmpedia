package io.woong.filmpedia.util

import android.net.Uri

object UriUtil {

    private const val TMDB_IMAGE_BASE: String = "https://image.tmdb.org/t/p/original"
    private const val FACEBOOK_BASE: String = "https://www.facebook.com/"
    private const val INSTAGRAM_BASE: String = "https://www.instagram.com/"
    private const val TWITTER_BASE: String = "https://twitter.com/"
    private const val TMDB_BASE: String = "https://www.themoviedb.org/movie/"
    private const val IMDB_BASE: String = "https://www.imdb.com/title/"

    fun getImageUrl(path: String?): String? {
        return path?.let {
            TMDB_IMAGE_BASE + path
        }
    }

    fun getSocialUri(path: String?, type: SocialType): Uri? {
        return if (path != null) {
            when (type) {
                SocialType.FACEBOOK -> Uri.parse(FACEBOOK_BASE + path)
                SocialType.INSTAGRAM -> Uri.parse(INSTAGRAM_BASE + path)
                SocialType.TWITTER -> Uri.parse(TWITTER_BASE + path)
                SocialType.TMDB -> Uri.parse(TMDB_BASE + path)
                SocialType.IMDB -> Uri.parse(IMDB_BASE + path)
                SocialType.OTHER -> Uri.parse(path)
            }
        } else {
            null
        }
    }

    enum class SocialType {
        FACEBOOK, INSTAGRAM, TWITTER, TMDB, IMDB, OTHER
    }
}
