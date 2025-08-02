package pokedex.com.pokedex.request

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface PokeApiService {

    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id") id: Int): Call<PokemonResponse>
}


