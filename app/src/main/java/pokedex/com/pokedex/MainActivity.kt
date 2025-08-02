package pokedex.com.pokedex

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import pokedex.com.pokedex.database.PokemonDbHelper
import pokedex.com.pokedex.views.PokeViewActivity
import pokedex.com.pokedex.views.SeusPokemonsActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: PokemonDbHelper

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = PokemonDbHelper(this)

        val btnVerTodos = findViewById<TextView>(R.id.btnVerTodos)
        btnVerTodos.setOnClickListener {
            val intent = Intent(this, SeusPokemonsActivity::class.java)
            startActivity(intent)
        }

        carregarPokemonsNoContainer()
        mostrarPokemonSorteado()

        val searchBar = findViewById<EditText>(R.id.search_bar)


        searchBar.setOnEditorActionListener { v, actionId, event ->
            executarPesquisa(searchBar.text.toString())
            true
        }


        searchBar.setOnTouchListener { v, event ->
            if (event.action == android.view.MotionEvent.ACTION_UP) {
                val drawableStartWidth = searchBar.compoundDrawables[0]?.bounds?.width() ?: 0

                if (event.x <= (searchBar.paddingStart + drawableStartWidth)) {
                    executarPesquisa(searchBar.text.toString())
                    return@setOnTouchListener true
                }
            }
            false
        }


    }

    private fun executarPesquisa(texto: String) {
        val numero = texto.trim().toIntOrNull()
        if (numero != null && numero > 0) {
            val intent = Intent(this, PokeViewActivity::class.java)
            intent.putExtra("pokemon_number", numero)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Digite um número válido para pesquisar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarPokemonsNoContainer() {
        val container = findViewById<LinearLayout>(R.id.containerPokemons)
        container.removeAllViews()

        val pokemonsIds = dbHelper.buscarTodosPokemons().take(6)

        for (id in pokemonsIds) {
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8))
                layoutParams = LinearLayout.LayoutParams(dpToPx(120), LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    marginEnd = dpToPx(8)
                }
                gravity = Gravity.CENTER
                elevation = 4f
                setBackgroundColor(Color.WHITE)
            }

            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(dpToPx(80), dpToPx(80))
                scaleType = ImageView.ScaleType.FIT_CENTER
                val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                Glide.with(this@MainActivity).load(imageUrl).into(this)
                contentDescription = "Pokemon $id"
            }

            val nomeText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(0, dpToPx(8), 0, 0)
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

            container.addView(card)
        }
    }

    private fun mostrarPokemonSorteado() {
        val pokemonsIds = dbHelper.buscarTodosPokemons()
        if (pokemonsIds.isEmpty()) return

        val sorteadoId = pokemonsIds.random()

        val imageView = findViewById<ImageView>(R.id.pokemon_do_dia_image)
        val nomeText = findViewById<TextView>(R.id.pokemon_do_dia_nome)
        val descricaoText = findViewById<TextView>(R.id.pokemon_do_dia_descricao)

        val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$sorteadoId.png"
        Glide.with(this).load(imageUrl).into(imageView)
        imageView.contentDescription = "Pokemon sorteado #$sorteadoId"

        nomeText.text = "Pokémon sorteado #$sorteadoId"

        imageView.setOnClickListener {
            val intent = Intent(this, PokeViewActivity::class.java)
            intent.putExtra("pokemon_number", sorteadoId)
            startActivity(intent)
        }
        nomeText.setOnClickListener {
            val intent = Intent(this, PokeViewActivity::class.java)
            intent.putExtra("pokemon_number", sorteadoId)
            startActivity(intent)
        }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()
}
