package com.example.goshtflix.data.network.enums

enum class MovieCategory {
    NOW_PLAYING,
    TOP_RATED,
    UPCOMING,
    POPULAR,
    SEARCH;

    companion object {
        fun fromString(value: String): MovieCategory? {
            return when (value) {
                "now_playing" -> NOW_PLAYING
                "top_rated" -> TOP_RATED
                "upcoming" -> UPCOMING
                "popular" -> POPULAR
                "search" -> SEARCH
                else -> null
            }
        }
    }
}