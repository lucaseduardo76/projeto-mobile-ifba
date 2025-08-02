package pokedex.com.pokedex.views

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Typeface
import android.view.Gravity
import android.content.res.ColorStateList
import android.graphics.Color
import pokedex.com.pokedex.R
import pokedex.com.pokedex.database.PokemonDbHelper
import com.bumptech.glide.Glide

class SeusPokemonsActivity : AppCompatActivity() {

    private lateinit var dbHelper: PokemonDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seus_pokemons)

        dbHelper = PokemonDbHelper(this)

        val btnVoltar = findViewById<ImageView>(R.id.btn_voltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        val inputNumero = findViewById<EditText>(R.id.inputNumeroPokemon)
        val btnAbrir = findViewById<ImageButton>(R.id.btnAbrirPokemon)
        val btnDel = findViewById<ImageButton>(R.id.btnDelPokemon)

        btnAbrir.setOnClickListener {
            val numero = inputNumero.text.toString().trim()

            if (numero.isNotEmpty()) {
                val id = numero.toIntOrNull()
                if (id != null && id > 0) {
                    if (id > 1008) {
                        Toast.makeText(this, "Número máximo permitido é 1008!", Toast.LENGTH_SHORT).show()
                    } else {
                        val sucesso = dbHelper.adicionarPokemon(id)
                        if (sucesso) {
                            Toast.makeText(this, "Pokémon adicionado!", Toast.LENGTH_SHORT).show()
                            inputNumero.text.clear()
                            mostrarPokemonsNoGrid()
                        } else {
                            Toast.makeText(this, "Você já adicionou esse Pokémon!", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Digite um número válido!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Digite o número do Pokémon!", Toast.LENGTH_SHORT).show()
            }
        }

        btnDel.setOnClickListener {
            val numero = inputNumero.text.toString().trim()

            if (numero.isNotEmpty()) {
                val id = numero.toIntOrNull()
                if (id != null && id > 0) {
                   val sucesso = dbHelper.removerPokemonPorId(id)
                    if (sucesso) {
                        Toast.makeText(this, "Pokémon removido!", Toast.LENGTH_SHORT).show()
                        inputNumero.text.clear()
                        mostrarPokemonsNoGrid()
                    }
                } else {
                    Toast.makeText(this, "Digite um número válido!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Digite o número do Pokémon!", Toast.LENGTH_SHORT).show()
            }
        }

        mostrarPokemonsNoGrid()
    }

    private fun mostrarPokemonsNoGrid() {
        val grid = findViewById<GridLayout>(R.id.gridPokemons)
        grid.removeAllViews()
        val pokemonsIds = dbHelper.buscarTodosPokemons()

        for (id in pokemonsIds) {
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(8.dpToPx(), 8.dpToPx(), 8.dpToPx(), 8.dpToPx())
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = LinearLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(6.dpToPx(), 6.dpToPx(), 6.dpToPx(), 6.dpToPx())
                }
                gravity = Gravity.CENTER
                elevation = 4f
                setBackgroundColor(Color.WHITE)
                backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FAFAFA"))
            }

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(80.dpToPx(), 80.dpToPx())
                scaleType = ImageView.ScaleType.FIT_CENTER
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                Glide.with(this@SeusPokemonsActivity).load(imageUrl).into(this)
                contentDescription = "Pokemon $id"
            }

            val nomeText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(0, 8.dpToPx(), 0, 0)
                textSize = 14f
                setTypeface(typeface, Typeface.BOLD)
                text = "Pokémon #$id"
            }

            val numeroText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textSize = 12f
                setTextColor(Color.parseColor("#757575"))
                text = "#${id.toString().padStart(3, '0')}"
            }


            card.addView(imageView)
            card.addView(nomeText)
            card.addView(numeroText)

            card.setOnClickListener {
                val intent = Intent(this, PokeViewActivity::class.java)
                intent.putExtra("pokemon_number", id)
                startActivity(intent)
            }

            grid.addView(card)
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}
