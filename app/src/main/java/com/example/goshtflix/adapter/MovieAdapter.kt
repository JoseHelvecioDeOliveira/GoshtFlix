package com.example.goshtflix.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.goshtflix.R
import com.example.goshtflix.databinding.ItemMovieBinding
import com.example.goshtflix.model.Movie
import coil.load


class MovieAdapter(private val onClick: (Movie) -> Unit, private val onFavoriteClick: (Movie) -> Unit) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, onClick, onFavoriteClick)
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie, onClick: (Movie) -> Unit, onFavoriteClick: (Movie) -> Unit) {

            //Definindo o título
            binding.title.text = movie.title

            //Definindo o clique no favoritos
            binding.favoriteIcon.setOnClickListener{
                binding.favoriteIcon.setImageResource(R.drawable.ic_favorito_seelcionado)
            }

            // Montar a URL completa para o poster
            val posterUrl = movie.poster_path?.let { "https://image.tmdb.org/t/p/w500$it" }
                ?: "https://via.placeholder.com/500x750?text=Imagem+Indisponível"

            // Carregar a imagem do poster usando o Coil
            binding.poster.load(posterUrl) {
                crossfade(true) // Habilitar animação de transição
                placeholder(R.drawable.placeholder)
                error(R.drawable.ic_error)
            }

            binding.overview.text = movie.overview

            // Exibir a data de lançamento
            movie.release_date?.let {
                val formattedDate = formatDate(it)
                binding.tvDataItem.text = formattedDate
            }

            // Configurar o clique na view
            binding.root.setOnClickListener { onClick(movie) }
        }

        private fun formatDate(date: String): String {

            val dateParts = date.split("-")
            if (dateParts.size == 3) {
                return "${dateParts[2]}/${dateParts[1]}/${dateParts[0]}"
            }
            return date // Caso não consiga formatar, retorna a data original
        }

    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            // Adicionando log para verificar o ID dos filmes
            Log.d("MovieDiffCallback", "Comparando ID: ${oldItem.id} com ${newItem.id}")
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            // Adicionando log para verificar o conteúdo dos filmes
            Log.d("MovieDiffCallback", "Comparando conteúdo de: ${oldItem.title} com ${newItem.title}")
            return oldItem == newItem
        }
    }
}