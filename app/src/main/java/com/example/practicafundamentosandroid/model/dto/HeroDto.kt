package  com.example.practicafundamentosandroid.model.dto

data class HeroDto(
    val id: String,
    val photo: String,
    var favorite: Boolean,
    val name: String,
    val description: String,
)