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
                    val movie = response.body()

                    // Atualize o LiveData com os detalhes do filme
                    movie?.let {
                        _movieDetails.value = it

                        // Processar o campo de orçamento (budget)
                        val budget = it.budget?.let { budgetValue ->
                            "R$ ${budgetValue.formatCurrency()}"
                        } ?: "Não disponível"

                        // Processar os gêneros (genres)
                        val genres = it.genres?.joinToString { genre -> genre.name } ?: "Sem gênero"

                        // Atualizar os detalhes
                        _movieDetails.value = _movieDetails.value?.copy(
                            budget = it.budget,
                            genres = it.genres
                        )


                    }
                } else {
                    Log.e("MovieDetailViewModel", "Erro na resposta da API: ${response.message()}")
                }

                // Se a tradução não estiver na resposta principal, pegue do endpoint de traduções
                val translationResponse = ApiClient.apiService.getMovieTranslations(apiKey, movieId)
                if (translationResponse.isSuccessful) {
                    val translations = translationResponse.body()
                    translations?.let {
                        val ptTranslation = it.find { translation -> translation.iso_639_1 == "pt" }
                        ptTranslation?.let { translation ->
                            _movieDetails.value?.apply {
                                title = translation.data.title
                                overview = translation.data.overview
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("MovieDetailViewModel", "Erro na requisição: ${e.localizedMessage}")
            }
        }
    }

    // Função auxiliar para formatar valores de orçamento
    private fun Int.formatCurrency(): String {
        return "%,.2f".format(this.toDouble()).replace(",", ".")
    }
}
