package dev.carlosgsi.crunchytracker

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dev.carlosgsi.crunchytracker.adapter.AnimeAdapter
import dev.carlosgsi.crunchytracker.api.RetrofitClient
import dev.carlosgsi.crunchytracker.databinding.ActivityMainBinding
import dev.carlosgsi.crunchytracker.model.Anime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AnimeAdapter
    private var listaSeries = mutableListOf<Anime>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarRecyclerView()

        // Eliminamos probarConexionBasica() y reactivamos Jikan
        obtenerSeriesEnEmision()
    }

    private fun configurarRecyclerView() {
        // Al pulsar +1, actualizamos el modelo y notificamos solo a esa fila
        adapter = AnimeAdapter(listaSeries) { posicion ->
            val anime = listaSeries[posicion]
            // Validamos que no se puedan sumar más capítulos de los totales (si totales > 0)
            if (anime.episodiosTotales == 0 || anime.episodiosVistos < anime.episodiosTotales) {
                anime.episodiosVistos++
                adapter.notifyItemChanged(posicion)
            }
        }

        binding.recyclerViewSeries.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSeries.adapter = adapter
    }

    private fun obtenerSeriesEnEmision() {
        // lifecycleScope lanza la corrutina ligada a la vida de esta pantalla.
        // Si cierras la app antes de que cargue la API, la descarga se cancela sola (evita cuelgues de memoria).
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Hacemos la petición de red (esto bloquea el hilo IO, pero no la pantalla)
                val respuesta = RetrofitClient.apiService.getTrendingAnime()

                if (respuesta.isSuccessful) {
                    // Extraemos la lista 'data' de la respuesta JSON (o una lista vacía si es null)
                    val animesDto = respuesta.body()?.data ?: emptyList()

                    // Convertimos la lista de AnimeDto (red) a nuestra clase Anime (local)
                    val animesLocales = animesDto.map { it.toAnimeLocal() }

                    // Regresamos al hilo principal para actualizar la interfaz gráfica
                    withContext(Dispatchers.Main) {
                        listaSeries.clear()
                        listaSeries.addAll(animesLocales)
                        // Avisamos al adaptador para que dibuje las tarjetas
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    // Si el servidor devuelve error 404, 500, etc.
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MainActivity, "Error del servidor: ${respuesta.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Si el móvil no tiene internet o no encuentra la URL
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
                    Log.e("API_ERROR", "Error al cargar series", e)
                }
            }
        }
    }
}