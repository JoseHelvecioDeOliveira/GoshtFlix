package com.example.goshtflix.data.network

import MovieTranslationResponse
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

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MovieResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("language") language: String
    ): Response<MovieResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
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
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<MovieTranslationResponse>
}
