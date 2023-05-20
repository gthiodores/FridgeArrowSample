package model

import arrow.optics.optics

@optics
data class Material(
    val id: Int,
    val name: String,
) {
    companion object
}

val materials = listOf(
    Material(id = 1, name = "Egg"),
    Material(id = 2, name = "Flour"),
    Material(id = 3, name = "Milk"),
    Material(id = 4, name = "Bacon"),
)