package dev.carlosgsi.crunchytracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dev.carlosgsi.crunchytracker.adapter.AnimeAdapter
import dev.carlosgsi.crunchytracker.api.RetrofitClient
import dev.carlosgsi.crunchytracker.database.AnimeDao
import dev.carlosgsi.crunchytracker.database.AppDatabase
import dev.carlosgsi.crunchytracker.databinding.ActivityMainBinding
import dev.carlosgsi.crunchytracker.model.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AnimeAdapter
    private lateinit var animeDao: AnimeDao
    private var listaSeries = mutableListOf<Anime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos la conexión con la tabla local
        animeDao = AppDatabase.getDatabase(this).animeDao()

        configurarRecyclerView()
        configurarBuscador()

        // Al arrancar, comprobamos primero si el usuario ya tiene series guardadas
        cargarMisSeriesOPopulares()
    }

    private fun configurarRecyclerView() {
        adapter = AnimeAdapter(listaSeries) { posicion ->
            val anime = listaSeries[posicion]
            if (anime.episodiosTotales == 0 || anime.episodiosVistos < anime.episodiosTotales) {
                anime.episodiosVistos++
                adapter.notifyItemChanged(posicion)

                // Guardamos el progreso en SQLite en segundo plano
                lifecycleScope.launch(Dispatchers.IO) {
                    animeDao.guardarOActualizar(anime)
                }
            }
        }

        binding.recyclerViewSeries.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSeries.adapter = adapter
    }

    private fun cargarMisSeriesOPopulares() {
        lifecycleScope.launch(Dispatchers.IO) {
            val guardadas = animeDao.obtenerMisSeries()

            if (guardadas.isNotEmpty()) {
                // Si ya sigues series, mostramos tu lista personal al abrir la app
                withContext(Dispatchers.Main) {
                    binding.tvTitulo.text = "Mis Series"
                    listaSeries.clear()
                    listaSeries.addAll(guardadas)
                    adapter.notifyDataSetChanged()
                }
            } else {
                // Si la base de datos está vacía, descargamos el catálogo popular de Kitsu
                obtenerSeriesEnEmision()
            }
        }
    }

    private fun configurarBuscador() {
        binding.etBuscador.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = textView.text.toString().trim()
                if (query.isNotEmpty()) {
                    buscarAnimeEnKitsu(query)
                } else {
                    // Si busca con el texto vacío, vuelve a mostrar su lista guardada
                    cargarMisSeriesOPopulares()
                }
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(textView.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    private fun obtenerSeriesEnEmision() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val respuesta = RetrofitClient.apiService.getTrendingAnime()
                if (respuesta.isSuccessful) {
                    val animesDto = respuesta.body()?.data ?: emptyList()
                    val animesConProgreso = cruzarConBaseDeDatos(animesDto.map { it.toAnimeLocal() })

                    withContext(Dispatchers.Main) {
                        binding.tvTitulo.text = "Populares en Kitsu"
                        listaSeries.clear()
                        listaSeries.addAll(animesConProgreso)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "Error al cargar series", e)
                }
            }
        }
    }

    private fun buscarAnimeEnKitsu(query: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val respuesta = RetrofitClient.apiService.buscarAnime(query)
                if (respuesta.isSuccessful) {
                    val animesDto = respuesta.body()?.data ?: emptyList()
                    val animesConProgreso = cruzarConBaseDeDatos(animesDto.map { it.toAnimeLocal() })

                    withContext(Dispatchers.Main) {
                        binding.tvTitulo.text = "Resultados: $query"
                        listaSeries.clear()
                        listaSeries.addAll(animesConProgreso)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Error en la búsqueda", Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "Error buscando anime", e)
                }
            }
        }
    }

    // Si un anime que viene de Kitsu ya está en nuestra BD local, respetamos los episodios vistos guardados
    private suspend fun cruzarConBaseDeDatos(listaRed: List<Anime>): List<Anime> {
        return listaRed.map { animeRed ->
            val animeGuardado = animeDao.obtenerPorId(animeRed.id)
            if (animeGuardado != null) {
                animeRed.episodiosVistos = animeGuardado.episodiosVistos
            }
            animeRed
        }
    }
}