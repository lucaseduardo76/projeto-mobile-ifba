package pokedex.com.pokedex

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SeusPokemonsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.seus_pokemons)

        val btnVoltar = findViewById<ImageView>(R.id.btn_voltar)
        btnVoltar.setOnClickListener {
            finish()
        }
    }
}
