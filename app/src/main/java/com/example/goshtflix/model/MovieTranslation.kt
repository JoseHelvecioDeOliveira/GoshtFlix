package com.example.goshtflix.model

data class MovieTranslation(
    val iso_639_1: String,
    val name: String,
    val english_name: String,
    val data: MovieTranslationData
)

data class MovieTranslationData(
    val title: String,
    val overview: String
)
