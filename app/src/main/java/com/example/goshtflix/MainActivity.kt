package com.example.goshtflix

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.goshtflix.activity.MovieDetailActivity
import com.example.goshtflix.adapter.MovieAdapter
import com.example.goshtflix.databinding.ActivityMainBinding
import com.example.goshtflix.viewModel.MovieViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var movieAdapter: MovieAdapter
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieAdapter = MovieAdapter {
            val intent = Intent(this, MovieDetailActivity::class.java).apply {
                putExtra("movieId", it.id)
                putExtra("movieOverview", it.overview)
                putExtra("movieTitle", it.title)
                putExtra("moviePoster", it.poster_path)
                putExtra("movieReleaseDate", it.release_date)
                putExtra("movieBudget", it.budget)
                putExtra("movieGenres", it.genres?.joinToString(", ") ?: "Sem Gênero") // Passando os gêneros como string
            }
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = movieAdapter
        }

        viewModel.popularMovies.observe(this, Observer { movies ->
            movieAdapter.submitList(movies)
        })

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchMovies(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        viewModel.fetchPopularMovies()
    }
}
