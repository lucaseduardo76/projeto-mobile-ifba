package pokedex.com.pokedex

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔥 Interação para mudar de tela
        val btnVerTodos = findViewById<TextView>(R.id.btnVerTodos)
        btnVerTodos.setOnClickListener {
            val intent = Intent(this, SeusPokemonsActivity::class.java)
            startActivity(intent)
        }

        val cardPokemon = findViewById<LinearLayout>(R.id.card_pokemon)
        val cardPokemon1 = findViewById<LinearLayout>(R.id.card_pokemon1)
        val cardPokemon2 = findViewById<LinearLayout>(R.id.card_pokemon2)

        cardPokemon.setOnClickListener {
            val intent = Intent(this, PokeViewActivity::class.java)
            startActivity(intent)
        }

        cardPokemon1.setOnClickListener {
            val intent = Intent(this, PokeViewActivity::class.java)
            startActivity(intent)
        }

        cardPokemon2.setOnClickListener {
            val intent = Intent(this, PokeViewActivity::class.java)
            startActivity(intent)
        }
    }
}
