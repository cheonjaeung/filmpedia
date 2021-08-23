package io.woong.filmpedia.data

import com.google.gson.annotations.SerializedName

data class PeopleTranslations(
    val id: Int,
    val translations: List<Translation>
) {
    data class Translation(
        @SerializedName("iso_3166_1") val iso3166_1: String,
        @SerializedName("iso_639_1") val iso639_1: String,
        @SerializedName("name") val languageName: String,
        @SerializedName("english_name") val languageEnglishName: String,
        @SerializedName("data") val translated: Data
    ) {
        data class Data(
            val biography: String
        )
    }
}
