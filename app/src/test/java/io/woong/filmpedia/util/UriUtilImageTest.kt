package io.woong.filmpedia.util

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class UriUtilImageTest {

    private lateinit var tmdbImageBase: String

    @Before
    fun init() {
        val imageBaseField = UriUtil.javaClass.getDeclaredField("TMDB_IMAGE_BASE")
        imageBaseField.isAccessible = true
        tmdbImageBase = imageBaseField.get(UriUtil) as String
    }

    @Test
    fun testImageUrl() {
        val testImagePath = "/test.jpg"

        val path = UriUtil.getImageUrl(testImagePath)

        assertEquals(path.toString(), tmdbImageBase + testImagePath)
    }

    @Test
    fun testNullImageUrl() {
        val path = UriUtil.getImageUrl(null)
        assertEquals(path, null)
    }
}
