package pokedex.com.pokedex.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PokemonDbHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "pokemon.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "meus_pokemons"
        const val COLUMN_ID = "id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun adicionarPokemon(id: Int): Boolean {
        val db = writableDatabase

        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        val existe = cursor.moveToFirst()
        cursor.close()

        if (existe) return false

        val values = ContentValues().apply {
            put(COLUMN_ID, id)
        }

        val result = db.insert(TABLE_NAME, null, values)
        return result != -1L
    }

    fun buscarTodosPokemons(): List<Int> {
        val pokemons = mutableListOf<Int>()
        val db = readableDatabase

        val cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_ID),
            null, null, null, null,
            "$COLUMN_ID ASC"
        )

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                    pokemons.add(id)
                } while (it.moveToNext())
            }
        }

        return pokemons
    }

    fun removerPokemonPorId(id: Int): Boolean {
        val db = writableDatabase
        val deletedRows = db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
        return deletedRows > 0
    }
}