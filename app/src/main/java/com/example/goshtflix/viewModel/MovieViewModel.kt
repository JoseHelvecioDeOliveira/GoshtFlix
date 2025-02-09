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

    private val _topRatedMovies = MutableLiveData<List<Movie>>()
    val topRatedMovies: LiveData<List<Movie>> = _topRatedMovies

    private val _nowPlayingMovies = MutableLiveData<List<Movie>>()
    val nowPlayingMovies: LiveData<List<Movie>> = _nowPlayingMovies

    private val _upcomingMovies = MutableLiveData<List<Movie>>()
    val upcomingMovies: LiveData<List<Movie>> = _upcomingMovies

    private val _searchResults = MutableLiveData<List<Movie>>()
    val searchResults: LiveData<List<Movie>> = _searchResults

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val apiKey = "1b4a8c713d7cdd9e2a01e5d4eceb2842"
    private val language = "pt-BR"

    private var currentPage = 1

    fun fetchTopRatedMovies() {
        fetchMovies(
            fetchFunction = { ApiClient.apiService.getTopRatedMovies(apiKey, currentPage, language) },
            liveData = _topRatedMovies,
            appendResults = true
        )
    }

    fun fetchNowPlayingMovies() {
        fetchMovies(
            fetchFunction = { ApiClient.apiService.getNowPlayingMovies(apiKey, currentPage, language) },
            liveData = _nowPlayingMovies,
            appendResults = true
        )
    }

    fun fetchUpcomingMovies() {
        fetchMovies(
            fetchFunction = { ApiClient.apiService.getUpcomingMovies(apiKey, currentPage, language) },
            liveData = _upcomingMovies,
            appendResults = true
        )
    }

    fun fetchPopularMovies() {
        viewModelScope.launch {
            _isLoading.postValue(true)  // Exibe o ProgressBar
            val response = ApiClient.apiService.getPopularMovies(apiKey, currentPage, language)
            if (response.isSuccessful) {
                _popularMovies.value = response.body()?.results ?: emptyList()
            }
            _isLoading.postValue(false)  // Esconde o ProgressBar após o carregamento
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            val response = ApiClient.apiService.searchMovies(apiKey, query, currentPage, language)
            if (response.isSuccessful) {
                _popularMovies.value = response.body()?.results ?: emptyList()
            }
        }
    }

    fun loadMorePopularMovies() {
        currentPage++
        fetchPopularMovies()
    }

    private fun fetchMovies(
        fetchFunction: suspend () -> retrofit2.Response<com.example.goshtflix.model.MovieResponse>,
        liveData: MutableLiveData<List<Movie>>,
        appendResults: Boolean
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = fetchFunction()
                if (response.isSuccessful) {
                    val newMovies = response.body()?.results ?: emptyList()
                    liveData.postValue(if (appendResults) liveData.value.orEmpty() + newMovies else newMovies)
                } else {
                    _errorMessage.postValue("Erro: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Erro de conexão: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

}
