package com.example.goshtflix.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goshtflix.data.network.ApiClient
import com.example.goshtflix.model.MovieDetails
import kotlinx.coroutines.launch

class MovieDetailViewModel : ViewModel() {

    private val _movieDetails = MutableLiveData<MovieDetails>()
    val movieDetails: LiveData<MovieDetails> = _movieDetails

    private val apiKey = "1b4a8c713d7cdd9e2a01e5d4eceb2842"
    private val language = "pt-BR"

    fun fetchMovieDetails(movieId: Int) {
        viewModelScope.launch {
            try {
                // Primeira requisição para os detalhes do filme
                val response = ApiClient.apiService.getMovieDetails(apiKey, movieId, language)
                if (response.isSuccessful) {
                    _movieDetails.value = response.body()

                    // Se a tradução não estiver na resposta principal, pegue do endpoint de traduções
                    val translationResponse = ApiClient.apiService.getMovieTranslations(apiKey, movieId)
                    if (translationResponse.isSuccessful) {
                        val translations = translationResponse.body()
                        translations?.let {
                            // Busque a tradução em português (pt-BR)
                            val ptTranslation = it.find { translation -> translation.iso_639_1 == "pt" }
                            ptTranslation?.let { translation ->
                                _movieDetails.value?.apply {
                                    title = translation.data.title
                                    overview = translation.data.overview
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("MovieDetail", "Erro na requisição: ${e.localizedMessage}")
            }
        }
    }

}
