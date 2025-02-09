package com.example.goshtflix

import android.annotation.SuppressLint
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
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.goshtflix.activity.MovieDetailActivity
import com.example.goshtflix.adapter.MovieAdapter
import com.example.goshtflix.databinding.ActivityMainBinding
import com.example.goshtflix.viewModel.MovieViewModel
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.google.android.material.internal.ViewUtils.showKeyboard

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel: MovieViewModel by viewModels()
    private var progressVisibleForAtLeast5Seconds = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        setupSearchView()

        viewModel.fetchPopularMovies()
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
                        // Carregar mais filmes quando o usuário rolar até o final
                        viewModel.loadMorePopularMovies()
                    }
                }
            })
        }
    }

    private fun setupObservers() {
        // Observa os filmes populares
        viewModel.popularMovies.observe(this) { movies ->
            movieAdapter.submitList(movies)
        }

        // Observa os resultados da pesquisa
        viewModel.searchResults.observe(this) { movies ->
            if (movies.isEmpty()) {
                Toast.makeText(this, "Nenhum filme encontrado para sua busca.", Toast.LENGTH_SHORT).show()
            }
            movieAdapter.submitList(movies)
        }

        // Observa o estado de carregamento
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showProgress()
            } else {
                hideProgress()
            }
        }

        // Observa mensagens de erro
        viewModel.errorMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
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
            // Chama o ViewModel para buscar todos os filmes para o termo de pesquisa
            viewModel.searchMovies(query)
            hideKeyboard()
            binding.searchView.clearFocus() // Remove o foco do EditText após a pesquisa
        } else {
            viewModel.fetchPopularMovies()
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

    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.constraintSearch.visibility = View.GONE
    }

    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.constraintSearch.visibility = View.VISIBLE
    }
}