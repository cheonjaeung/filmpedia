package io.woong.filmpedia

import android.app.Application
import android.os.Build

class FilmpediaApp : Application() {

    val tmdbApiKey: String
        get() = BuildConfig.API_KEY

    val language: String
        get() {
            val locale = if (Build.VERSION.SDK_INT >= 24) {
                resources.configuration.locales[0]
            } else {
                resources.configuration.locale
            }

            return when {
                locale == null -> {
                    "en-US"
                }
                locale.toString().startsWith("en") -> {
                    "en-US"
                }
                locale.toString().startsWith("ko") -> {
                    "ko-KR"
                }
                else -> {
                    "en-US"
                }
            }
        }

    val region: String
        get() {
            val locale = if (Build.VERSION.SDK_INT >= 24) {
                resources.configuration.locales[0]
            } else {
                resources.configuration.locale
            }

            return when (locale) {
                null -> {
                    "US"
                }
                else -> {
                    locale.country
                }
            }
        }
}
