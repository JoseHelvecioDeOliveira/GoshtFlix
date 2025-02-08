package com.example.goshtflix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.goshtflix.R
import com.example.goshtflix.databinding.ItemMovieBinding
import com.example.goshtflix.model.Movie
import coil.load


class MovieAdapter(private val onClick: (Movie) -> Unit) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, onClick)
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, onClick: (Movie) -> Unit) {
            // Definir o título
            binding.title.text = movie.title

            // Montar a URL completa para o poster
            val posterUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"

            // Carregar a imagem do poster usando o Coil
            binding.poster.load(posterUrl) {
                crossfade(true) // Habilitar animação de transição (opcional)
                placeholder(R.drawable.placeholder) // Imagem de placeholder enquanto carrega
                error(R.drawable.ic_error) // Imagem de erro, caso falhe no carregamento
            }

            binding.overview.text = movie.overview

            // Configurar o clique na view
            binding.root.setOnClickListener { onClick(movie) }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }
}
