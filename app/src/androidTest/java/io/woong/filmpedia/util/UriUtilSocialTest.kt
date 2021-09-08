package io.woong.filmpedia.util

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UriUtilSocialTest {

    private lateinit var facebookBase: String
    private lateinit var instagramBase: String
    private lateinit var twitterBase: String
    private lateinit var tmdbBase: String
    private lateinit var imdbBase: String

    @Before
    fun init() {
        val facebookField = UriUtil.javaClass.getDeclaredField("FACEBOOK_BASE")
        facebookField.isAccessible = true
        facebookBase = facebookField.get(UriUtil) as String

        val instagramField = UriUtil.javaClass.getDeclaredField("INSTAGRAM_BASE")
        instagramField.isAccessible = true
        instagramBase = instagramField.get(UriUtil) as String

        val twitterField = UriUtil.javaClass.getDeclaredField("TWITTER_BASE")
        twitterField.isAccessible = true
        twitterBase = twitterField.get(UriUtil) as String

        val tmdbField = UriUtil.javaClass.getDeclaredField("TMDB_BASE")
        tmdbField.isAccessible = true
        tmdbBase = tmdbField.get(UriUtil) as String

        val imdbField = UriUtil.javaClass.getDeclaredField("IMDB_BASE")
        imdbField.isAccessible = true
        imdbBase = imdbField.get(UriUtil) as String
    }

    @Test
    fun testFacebookUri() {
        val idList = listOf("test1", "test2", "test3")

        for (id in idList) {
            val uri = UriUtil.getSocialUri(id, UriUtil.SocialType.FACEBOOK)
            Assert.assertEquals(uri.toString(), facebookBase + id)
        }
    }

    @Test
    fun testInstagramUri() {
        val idList = listOf("test1", "test2", "test3")

        for (id in idList) {
            val uri = UriUtil.getSocialUri(id, UriUtil.SocialType.INSTAGRAM)
            Assert.assertEquals(uri.toString(), instagramBase + id)
        }
    }

    @Test
    fun testTwitterUri() {
        val idList = listOf("test1", "test2", "test3")

        for (id in idList) {
            val uri = UriUtil.getSocialUri(id, UriUtil.SocialType.TWITTER)
            Assert.assertEquals(uri.toString(), twitterBase + id)
        }
    }

    @Test
    fun testTmdbUri() {
        val idList = listOf("test1", "test2", "test3")

        for (id in idList) {
            val uri = UriUtil.getSocialUri(id, UriUtil.SocialType.TMDB)
            Assert.assertEquals(uri.toString(), tmdbBase + id)
        }
    }

    @Test
    fun testImdbUri() {
        val idList = listOf("test1", "test2", "test3")

        for (id in idList) {
            val uri = UriUtil.getSocialUri(id, UriUtil.SocialType.IMDB)
            Assert.assertEquals(uri.toString(), imdbBase + id)
        }
    }
}
