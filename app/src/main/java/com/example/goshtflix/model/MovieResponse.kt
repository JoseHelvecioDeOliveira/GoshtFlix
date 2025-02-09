package com.example.goshtflix.model

data class MovieResponse(
    val page: Int,
    val totalResults: Int,
    val totalPages: Int,
    val results: List<Movie>
)
