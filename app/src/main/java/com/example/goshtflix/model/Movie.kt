package com.example.goshtflix.model


data class Movie(
    val id: Int,
    var title: String,
    var overview: String,
    val release_date: String,
    val poster_path: String?,
    val genres: List<Genre>,
    var budget: Int?,
    val formattedBudget: String?,
    val linguagem: String,
    var isFavorite: Boolean
)