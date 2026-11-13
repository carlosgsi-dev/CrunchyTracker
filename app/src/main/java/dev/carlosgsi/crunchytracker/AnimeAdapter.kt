package dev.carlosgsi.crunchytracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.carlosgsi.crunchytracker.databinding.ItemAnimeBinding
import dev.carlosgsi.crunchytracker.model.Anime

class AnimeAdapter(
    private val animes: List<Anime>,
    private val onSumarClick: (Int) -> Unit
) : RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder>() {

    inner class AnimeViewHolder(val binding: ItemAnimeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(anime: Anime, posicion: Int) {
            binding.tvTituloSerie.text = anime.titulo
            binding.tvProgresoTexto.text = "Vistos: ${anime.episodiosVistos} / ${anime.episodiosTotales}"

            // Asignar progreso usando el método del modelo
            binding.progressBarSerie.progress = anime.calcularPorcentajeProgreso()

            // Cargar imagen con Glide
            Glide.with(binding.ivPortada.context)
                .load(anime.urlImagen)
                .centerCrop()
                .into(binding.ivPortada)

            // Evento del botón +1
            binding.btnSumar.setOnClickListener {
                if (anime.episodiosVistos < anime.episodiosTotales) {
                    onSumarClick(posicion)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding = ItemAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) {
        holder.bind(animes[position], position)
    }

    override fun getItemCount() = animes.size
}