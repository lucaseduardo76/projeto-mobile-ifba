package pokedex.com.pokedex.request

data class StatSummary(
    val base_stat: Int,
    val stat: StatName
)

data class StatName(
    val name: String
)