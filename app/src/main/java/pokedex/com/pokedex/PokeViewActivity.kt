package pokedex.com.pokedex

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PokeViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.poke_view) // nome do seu XML

        val btnVoltar = findViewById<ImageView>(R.id.btn_voltar)
        btnVoltar.setOnClickListener {
            finish()
        }
    }
}
