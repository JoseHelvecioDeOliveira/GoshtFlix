package com.example.goshtflix.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goshtflix.data.network.ApiClient
import com.example.goshtflix.data.network.TmdbApi
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

    fun fetchPopularMovies(resetList: Boolean = false) {
        fetchMovies(
            fetchFunction = { ApiClient.apiService.getPopularMovies(apiKey, currentPage, language) },
            liveData = _popularMovies,
            appendResults = false,  // Substitui a lista
            resetList = resetList // Passa o parâmetro para resetar a lista, se necessário
        )
    }

    fun fetchNowPlayingMovies(resetList: Boolean = false) {
        resetPagination()
        fetchMovies(
            fetchFunction = { ApiClient.apiService.getNowPlayingMovies(apiKey, currentPage, language) },
            liveData = _nowPlayingMovies,
            appendResults = false,  // Substitui a lista
            resetList = resetList // Passa o parâmetro resetList para limpar a lista
        )
    }

    fun fetchTopRatedMovies(resetList: Boolean = false) {
        resetPagination()
        fetchMovies(
            fetchFunction = { ApiClient.apiService.getTopRatedMovies(apiKey, currentPage, language) },
            liveData = _topRatedMovies,
            appendResults = false,  // Substitui a lista
            resetList = resetList // Passa o parâmetro resetList para limpar a lista
        )
    }

    fun fetchUpcomingMovies(resetList: Boolean = false) {
        resetPagination()
        fetchMovies(
            fetchFunction = { ApiClient.apiService.getUpcomingMovies(apiKey, currentPage, language) },
            liveData = _upcomingMovies,
            appendResults = false,  // Substitui a lista
            resetList = resetList // Passa o parâmetro resetList para limpar a lista
        )
    }


    fun searchMovies(query: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            if (query.isEmpty()) {
                _searchResults.postValue(emptyList())
                _isLoading.postValue(false)
                return@launch
            }

            var page = 1
            val allMovies = mutableListOf<Movie>()

            try {
                var hasMorePages = true

                while (hasMorePages) {
                    val response = ApiClient.apiService.searchMovies(apiKey, query, page, language)
                    if (response.isSuccessful) {
                        val result = response.body()
                        val movies = result?.results ?: emptyList()
                        Log.d("MovieViewModel", "Found ${movies.size} movies on page $page")
                        allMovies.addAll(movies)

                        // Verifica se há mais páginas
                        hasMorePages = movies.size < result?.totalResults ?: 0
                    } else {
                        _errorMessage.postValue("Erro: ${response.message()}")
                        break
                    }

                    page++
                }

                _searchResults.postValue(allMovies)
                Log.d("MovieViewModel", "Search results updated")

            } catch (e: Exception) {
                _errorMessage.postValue("Erro de conexão: ${e.message}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun loadMorePopularMovies() {
        if (isLoading.value == true) return // Impede múltiplas chamadas enquanto estiver carregando

        _isLoading.value = true // Inicia o carregamento

        currentPage++ // Incrementa a página para a próxima requisição
        fetchPopularMovies() // Chama a função para buscar os filmes da próxima página
    }

    private fun fetchMovies(
        fetchFunction: suspend () -> retrofit2.Response<com.example.goshtflix.model.MovieResponse>,
        liveData: MutableLiveData<List<Movie>>,
        appendResults: Boolean,
        resetList: Boolean = false  // Novo parâmetro para resetar a lista
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)

            // Se resetList for true, limpa a lista antes de carregar novos dados
            if (resetList) {
                liveData.postValue(emptyList()) // Limpa a lista
                resetPagination()  // Reinicia a paginação
            }

            try {
                val response = fetchFunction()
                if (response.isSuccessful) {
                    val newMovies = response.body()?.results ?: emptyList()
                    liveData.postValue(
                        if (appendResults) liveData.value.orEmpty() + newMovies else newMovies
                    )
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
    fun resetPagination() {
        currentPage = 1  // Reinicia a página
        _popularMovies.value = emptyList() // Limpa a lista de filmes populares
        _nowPlayingMovies.value = emptyList() // Limpa a lista de filmes em exibição
        _topRatedMovies.value = emptyList() // Limpa a lista de filmes mais bem avaliados
        _upcomingMovies.value = emptyList() // Limpa a lista de filmes futuros
    }


}
