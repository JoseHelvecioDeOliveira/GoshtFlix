package com.example.goshtflix.model

data class MovieTranslation(
    val iso_639_1: String,
    val data: TranslationData
)

data class TranslationData(
    val title: String,
    val overview: String
)
