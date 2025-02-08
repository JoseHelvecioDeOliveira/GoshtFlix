package com.example.goshtflix.data.network

import com.example.goshtflix.model.MovieDetails
import com.example.goshtflix.model.MovieResponse
import com.example.goshtflix.model.MovieTranslation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MovieResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Query("api_key") apiKey: String,
        @Query("id") movieId: Int,
        @Query("language") language: String
    ): Response<MovieDetails>

    @GET("movie/{id}/translations")
    suspend fun getMovieTranslations(
        @Query("api_key") apiKey: String,
        @Path("id") movieId: Int
    ): Response<List<MovieTranslation>>
}
