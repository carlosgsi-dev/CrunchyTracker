package dev.carlosgsi.crunchytracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dev.carlosgsi.crunchytracker.adapter.AnimeAdapter
import dev.carlosgsi.crunchytracker.databinding.ActivityMainBinding
import dev.carlosgsi.crunchytracker.model.Anime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AnimeAdapter
    private var listaSeries = mutableListOf<Anime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargarDatosPrueba()
        configurarRecyclerView()
    }

    private fun cargarDatosPrueba() {
        listaSeries.add(
            Anime(
                id = 1,
                titulo = "Jujutsu Kaisen",
                urlImagen = "https://cdn.myanimelist.net/images/anime/1171/109222.jpg",
                episodiosVistos = 5,
                episodiosTotales = 24
            )
        )
        listaSeries.add(
            Anime(
                id = 2,
                titulo = "Frieren",
                urlImagen = "https://cdn.myanimelist.net/images/anime/1015/138006.jpg",
                episodiosVistos = 28,
                episodiosTotales = 28
            )
        )
        listaSeries.add(
            Anime(
                id = 3,
                titulo = "Solo Leveling",
                urlImagen = "https://cdn.myanimelist.net/images/anime/1066/139598.jpg",
                episodiosVistos = 11,
                episodiosTotales = 12
            )
        )
    }

    private fun configurarRecyclerView() {
        // Al pulsar +1, actualizamos el modelo y notificamos solo a esa fila
        adapter = AnimeAdapter(listaSeries) { posicion ->
            val anime = listaSeries[posicion]
            anime.episodiosVistos++
            adapter.notifyItemChanged(posicion)
        }

        binding.recyclerViewSeries.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSeries.adapter = adapter
    }
}