package com.example.goshtflix.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.goshtflix.R
import com.example.goshtflix.databinding.ActivityMovieDetailBinding
import com.example.goshtflix.viewModel.MovieDetailViewModel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieDetailViewModel: MovieDetailViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        val voltar: ImageView = findViewById(R.id.ivVoltar)

        voltar.setOnClickListener {
            onBackPressed()
        }

        val backButton: MaterialButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Recuperando os dados passados pelo Intent
        val movieId = intent.getIntExtra("movieId", -1)
        val movieTitle = intent.getStringExtra("movieTitle") ?: "No Title"
        val movieOverview = intent.getStringExtra("movieOverview") ?: "No Overview"
        val moviePoster = intent.getStringExtra("moviePoster") ?: ""
        val movieReleaseDate = intent.getStringExtra("movieReleaseDate") ?: "No Release Date"
//        val movieBudget = intent.getIntExtra("movieBudget", -1)
//        val movieGenres = intent.getStringExtra("movieGenres") ?: "No Genres"

        // Formatar a data de lançamento
        val formattedReleaseDate = formatDate(movieReleaseDate)

        // Inicializar o ViewModel
        movieDetailViewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)

        movieDetailViewModel.fetchMovieDetails(movieId)

        // Inicializando as Views
        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val titleToolbar: MaterialToolbar = findViewById(R.id.toolbar)
        val overviewTextView: TextView = findViewById(R.id.tvOverview)
        val posterImageView: ImageView = findViewById(R.id.posterImageView)
        val tvTitulo: TextView = findViewById(R.id.tvTitulo)
        val movieDataTextView: TextView = findViewById(R.id.tvData)  // Aqui você vai exibir a data formatada
        //val movieGenresTextView: TextView = findViewById(R.id.tvGenero)

        // Atribuindo os valores às Views
        titleTextView.text = movieTitle
        tvTitulo.text = movieTitle
        titleToolbar.title = "GoshtFlix"
        overviewTextView.text = movieOverview
        //movieBudgetTextView.text = "R$ $movieBudget"
       // movieGenresTextView.text = movieGenres
        movieDataTextView.text = formattedReleaseDate  // Aqui a data formatada será exibida

        // Exibindo o poster do filme
        if (moviePoster.isNotEmpty()) {
            val posterUrl = "https://image.tmdb.org/t/p/w500$moviePoster"
            posterImageView.load(posterUrl) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.ic_error)
            }
        }

        // Observar mudanças nos detalhes do filme (se necessário)
        movieDetailViewModel.movieDetails.observe(this, Observer { movie ->
           // movieBudgetTextView.text = movie.formattedBudget
            //movieGenresTextView.text = movie.genres.toString()
        })
    }

    // Função para formatar a data
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Formato original da API
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Formato desejado

        return try {
            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: return "Data inválida") // Formatar a data
        } catch (e: Exception) {
            e.printStackTrace()
            "Data inválida" // Caso ocorra um erro
        }
    }
}