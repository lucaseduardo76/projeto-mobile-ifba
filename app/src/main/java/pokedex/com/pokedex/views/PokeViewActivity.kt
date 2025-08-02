package pokedex.com.pokedex.views

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import pokedex.com.pokedex.R
import pokedex.com.pokedex.request.PokemonResponse
import pokedex.com.pokedex.request.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokeViewActivity : AppCompatActivity() {

    private var numeroPokemonAtual = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poke_view)

        numeroPokemonAtual = intent.getIntExtra("pokemon_number", 1)

        val btnVoltar = findViewById<ImageView>(R.id.btn_voltar)
        val btnPrev = findViewById<ImageView>(R.id.btnPrev)
        val btnNext = findViewById<ImageView>(R.id.btnNext)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        val searchInput = findViewById<EditText>(R.id.searchInput)

        btnVoltar.setOnClickListener { finish() }

        btnPrev.setOnClickListener {
            if (numeroPokemonAtual > 1) {
                numeroPokemonAtual--
                carregarPokemon(numeroPokemonAtual)
            } else {
                Toast.makeText(this, "Esse é o primeiro Pokémon!", Toast.LENGTH_SHORT).show()
            }
        }

        btnNext.setOnClickListener {
            numeroPokemonAtual++
            carregarPokemon(numeroPokemonAtual)
        }

        btnSearch.setOnClickListener {
            val input = searchInput.text.toString().trim()
            if (input.isNotEmpty()) {
                try {
                    val numero = input.toInt()
                    numeroPokemonAtual = numero
                    carregarPokemon(numeroPokemonAtual)
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Digite um número válido!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Digite um número no campo de busca.", Toast.LENGTH_SHORT).show()
            }
        }

        carregarPokemon(numeroPokemonAtual)
    }


    private fun carregarPokemon(numero: Int) {
        val call = RetrofitClient.pokeApiService.getPokemonById(numero)

        call.enqueue(object : Callback<PokemonResponse> {
            override fun onResponse(call: Call<PokemonResponse>, response: Response<PokemonResponse>) {
                if (response.isSuccessful) {
                    val pokemon = response.body() ?: return

                    val mainLayout = findViewById<RelativeLayout>(R.id.main)
                    val tipo = pokemon.types.firstOrNull()?.type?.name ?: "unknow"
                    val corFundo = switchBackgroundColor(tipo)
                    mainLayout.setBackgroundColor(corFundo)

                    findViewById<TextView>(R.id.pokemonName).text = pokemon.name.replaceFirstChar { it.uppercase() }
                    findViewById<TextView>(R.id.pokemonType).text = tipo
                    findViewById<TextView>(R.id.pokemonNumber).text = "#${pokemon.id.toString().padStart(3, '0')}"

                    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
                    Glide.with(this@PokeViewActivity).load(imageUrl).into(findViewById(R.id.pokemonImage))

                    atualizarStatusNumeros(pokemon)
                } else {
                    Toast.makeText(this@PokeViewActivity, "Pokémon não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {
                Toast.makeText(this@PokeViewActivity, "Erro ao carregar Pokémon: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun atualizarStatusNumeros(pokemon: PokemonResponse) {
        val statMap = pokemon.stats.associate { it.stat.name to it.base_stat }

        findViewById<TextView>(R.id.hpValue).text = statMap["hp"]?.toString() ?: "0"
        findViewById<TextView>(R.id.attackValue).text = statMap["attack"]?.toString() ?: "0"
        findViewById<TextView>(R.id.defenseValue).text = statMap["defense"]?.toString() ?: "0"
        findViewById<TextView>(R.id.spAttackValue).text = statMap["special-attack"]?.toString() ?: "0"
        findViewById<TextView>(R.id.spDefenseValue).text = statMap["special-defense"]?.toString() ?: "0"
        findViewById<TextView>(R.id.speedValue).text = statMap["speed"]?.toString() ?: "0"
        findViewById<TextView>(R.id.pokemonHeight).text = "${pokemon.height}M"
    }

    private fun switchBackgroundColor(type: String): Int {
        return when (type.lowercase()) {
            "fire" -> 0xFFFF7402.toInt()
            "grass" -> 0xFF33A165.toInt()
            "steel" -> 0xFF00858A.toInt()
            "water" -> 0xFF0050AC.toInt()
            "psychic" -> 0xFFC90086.toInt()
            "ground" -> 0xFFC90086.toInt()
            "ice" -> 0xFF00BDCE.toInt()
            "flying" -> 0xFF5D4E75.toInt()
            "ghost" -> 0xFF4D5B64.toInt()
            "normal" -> 0xFF753845.toInt()
            "poison" -> 0xFF7E0058.toInt()
            "rock" -> 0xFF6E1A00.toInt()
            "fighting" -> 0xFF634136.toInt()
            "bug" -> 0xFF332165.toInt()
            "dark" -> 0xFF272625.toInt()
            "dragon" -> 0xFF7E0058.toInt()
            "electric" -> 0xFFBBA909.toInt()
            "fairy" -> 0xFFD31C81.toInt()
            "shadow" -> 0xFF29292C.toInt()
            "unknow" -> 0xFF757575.toInt()
            else -> 0xFF333333.toInt()
        }
    }
}
