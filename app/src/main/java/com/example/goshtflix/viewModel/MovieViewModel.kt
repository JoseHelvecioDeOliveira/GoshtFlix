package com.example.goshtflix.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goshtflix.data.network.ApiClient
import com.example.goshtflix.model.Movie
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val _popularMovies = MutableLiveData<List<Movie>>()
    val popularMovies: LiveData<List<Movie>> = _popularMovies

    private val apiKey = "1b4a8c713d7cdd9e2a01e5d4eceb2842"
    private val language = "pt-BR"

    fun fetchPopularMovies() {
        viewModelScope.launch {
            val response = ApiClient.apiService.getPopularMovies(apiKey, 1, language)
            if (response.isSuccessful) {
                _popularMovies.value = response.body()?.results ?: emptyList()
            }
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            val response = ApiClient.apiService.searchMovies(apiKey, query, 1, language)
            if (response.isSuccessful) {
                _popularMovies.value = response.body()?.results ?: emptyList()
            }
        }
    }
}
