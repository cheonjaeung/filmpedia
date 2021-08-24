package io.woong.filmpedia.util

import org.junit.Test
import org.junit.Assert.*

class DateUtilTest {
    @Test
    fun testGetAge() {
        val cases = listOf(
            Pair("2021-01-01", "2021-02-02"),
            Pair("1997-01-02", "2021-08-24"),
            Pair("1997-09-01", "2021-08-24"),
            Pair("1924-04-03", "2004-07-01")
        )

        val answers = listOf(
            0,
            24,
            23,
            80
        )

        for ((index, case) in cases.withIndex()) {
            val result = DateUtil.getAge(case.first, case.second)
            assertEquals(answers[index], result)
        }
    }
}