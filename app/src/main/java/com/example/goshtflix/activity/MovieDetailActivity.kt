package com.example.goshtflix.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.goshtflix.R
import com.example.goshtflix.databinding.ActivityMovieDetailBinding
import com.example.goshtflix.viewModel.MovieDetailViewModel

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Recuperando os dados passados pelo Intent
        val movieId = intent.getIntExtra("movieId", -1)
        val movieTitle = intent.getStringExtra("movieTitle") ?: "No Title"
        val movieOverview = intent.getStringExtra("movieOverview") ?: "No Overview"
        val moviePoster = intent.getStringExtra("moviePoster") ?: ""  // Pode ser o caminho da imagem

        // Inicializar o ViewModel
        movieDetailViewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)

        movieDetailViewModel.fetchMovieDetails(movieId)

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val overviewTextView: TextView = findViewById(R.id.overviewTextView)
        val posterImageView: ImageView = findViewById(R.id.posterImageView)

        titleTextView.text = movieTitle
        overviewTextView.text = movieOverview

        if (moviePoster.isNotEmpty()) {
            val posterUrl = "https://image.tmdb.org/t/p/w500$moviePoster"
            posterImageView.load(posterUrl) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.ic_error)
            }
        }
    }
}

