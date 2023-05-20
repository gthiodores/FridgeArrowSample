package model

import arrow.optics.optics

typealias Content = Ingredient

@optics
data class Fridge(val contents: List<Content>) {
    val mappedContents: Map<Int, Content>
        get() = contents.associateBy { item -> item.material.id }
    val mappedAmount: Map<Int, Measurement>
        get() = mappedContents.mapValues { (_, item) -> item.measurement }

    override fun toString(): String = "Fridge contains : ${contents.joinToString(separator = "\n")}"

    companion object
}

val defaultFridge = Fridge(
    contents = listOf(
        Content(
            material = Material(id = 1, name = "Egg"),
            measurement = Measurement(amount = 3.0, unit = SimpleUnit.PIECE),
        ),
        Content(
            material = Material(id = 4, name = "Bacon"),
            measurement = Measurement(amount = 2.0, unit = SimpleUnit.PIECE),
        ),
        Content(
            material = Material(id = 2, name = "Milk"),
            measurement = Measurement(amount = 1.0, unit = SimpleUnit.LITER),
        ),
    ),
)