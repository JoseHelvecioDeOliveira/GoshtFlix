package com.example.goshtflix.activity

import FilterBottomSheetFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.query
import com.example.goshtflix.adapter.MovieAdapter
import com.example.goshtflix.data.network.enums.MovieCategory
import com.example.goshtflix.databinding.ActivityMainBinding
import com.example.goshtflix.viewModel.MovieViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private var currentCategory: MovieCategory? = null
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupSearchView()

        viewModel.fetchPopularMovies(resetList = false)


        // Configura o botão de Categoria para abrir o BottomSheet
        binding.botaoCategoria.setOnClickListener {
            showFilterBottomSheet()
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter {
            val intent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra("movieId", it.id)
                putExtra("movieOverview", it.overview)
                putExtra("movieTitle", it.title)
                putExtra("moviePoster", it.poster_path)
                putExtra("movieReleaseDate", it.release_date)
                putExtra("movieBudget", it.budget)
                putExtra("movieGenres", it.genres?.joinToString(", ") ?: "Sem Gênero")
            }
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = movieAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!recyclerView.canScrollVertically(1)) {
                        showProgress()
                        Handler(Looper.getMainLooper()).postDelayed({
                            // Verifica se currentCategory é null e, se for, define a categoria como POPULAR
                            val category = currentCategory ?: MovieCategory.POPULAR

                            when (category) {
                                MovieCategory.SEARCH -> {
                                    val query = binding.searchView.text.toString()
                                    viewModel.loadMoreSearchResults(query)
                                }

                                MovieCategory.NOW_PLAYING -> viewModel.loadMoreMovies(category)
                                MovieCategory.TOP_RATED -> viewModel.loadMoreMovies(category)
                                MovieCategory.UPCOMING -> viewModel.loadMoreMovies(category)
                                MovieCategory.POPULAR -> viewModel.loadMoreMovies(category)

                            }
                        }, 2000)
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        // Observa os filmes populares
        viewModel.popularMovies.observe(this) { movies ->
            if (movies.isNotEmpty() && movieAdapter.currentList != movies) {
                movieAdapter.submitList(movies)  // Atualiza a lista apenas se os filmes mudaram
            }
        }

        viewModel.nowPlayingMovies.observe(this) { movies ->
            if (movies.isNotEmpty() && movieAdapter.currentList != movies) {
                movieAdapter.submitList(movies)
            }
        }

        viewModel.topRatedMovies.observe(this) { movies ->
            if (movies.isNotEmpty() && movieAdapter.currentList != movies) {
                movieAdapter.submitList(movies)
            }
        }

        viewModel.upcomingMovies.observe(this) { movies ->
            if (movies.isNotEmpty() && movieAdapter.currentList != movies) {
                movieAdapter.submitList(movies)
            }
        }

        // Observa os resultados da pesquisa
        viewModel.searchResults.observe(this) { movies ->
            if (movies.isEmpty()) {
                Toast.makeText(this, "Nenhum filme encontrado para sua busca.", Toast.LENGTH_SHORT)
                    .show()
            }
            if (movieAdapter.currentList != movies) {
                movieAdapter.submitList(movies)
            }
        }

        // Observa o estado de carregamento
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showProgress()
                Handler(Looper.getMainLooper()).postDelayed({
                    hideProgress()
                }, 2000)
            }
        }
        hideProgress()
    }


    private fun setupSearchView() {
        // Garante que ao clicar no constraintSearch, o EditText receba foco
        binding.searchView.setOnClickListener {
            binding.searchView.requestFocus()
            showKeyboard(binding.searchView)
        }

        // Captura a ação de pressionar "Enter" no teclado para fazer a busca
        binding.searchView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                executeSearch(binding.searchView.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun executeSearch(query: String) {
        if (query.isNotEmpty()) {
            currentCategory = MovieCategory.SEARCH

            // Chama o ViewModel para buscar todos os filmes para o termo de pesquisa
            viewModel.searchMovies(query)
            hideKeyboard()
            binding.searchView.clearFocus()
        } else {
            hideKeyboard()
            currentCategory = MovieCategory.POPULAR
            Toast.makeText(this, "Digite algo para buscar!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
    }

    private fun showFilterBottomSheet() {
        val bottomSheetFragment = FilterBottomSheetFragment { filter ->
            val selectedCategory = MovieCategory.fromString(filter)

            // Atualiza a categoria selecionada
            selectedCategory?.let {
                currentCategory = it
                viewModel.setCurrentCategory(it)
            }

            // Chama o método da API com base no filtro selecionado
            when (selectedCategory) {
                MovieCategory.NOW_PLAYING -> viewModel.fetchNowPlayingMovies(resetList = true)
                MovieCategory.TOP_RATED -> viewModel.fetchTopRatedMovies(resetList = true)
                MovieCategory.UPCOMING -> viewModel.fetchUpcomingMovies(resetList = true)
                else -> {}
            }
        }
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }


    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.favoriteIcon.visibility = View.GONE
        binding.botaoCategoria.visibility = View.GONE
        binding.constraintSearch.visibility = View.GONE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.constraintSearch.visibility = View.VISIBLE
        binding.favoriteIcon.visibility = View.VISIBLE
        binding.botaoCategoria.visibility = View.VISIBLE
    }
}