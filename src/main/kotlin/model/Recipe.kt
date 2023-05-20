package model

import arrow.core.Either
import arrow.core.Nel
import arrow.core.mapOrAccumulate
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure


class Recipe private constructor(
    val name: String,
    val ingredients: Set<Ingredient>,
) {
    companion object {
        operator fun invoke(
            name: String,
            ingredients: Set<Ingredient>,
        ): Either<Nel<IllegalIngredientAmount>, Recipe> = either {
            ingredients
                .mapOrAccumulate { item -> ensure(item) }
                .map { _ -> Recipe(name = name, ingredients = ingredients) }
                .bind()
        }

        private fun Raise<IllegalIngredientAmount>.ensure(ingredient: Ingredient) {
            ensure(ingredient.measurement.amount > 0.0) { IllegalIngredientAmount(ingredient) }
        }
    }
}

data class IllegalIngredientAmount(val ingredient: Ingredient) {
    override fun toString(): String = "${ingredient.material.name} was initialized with less or equal to 0 amount"
}

val recipes = mapOf(
    "Egg and Bacon" to Recipe(
        name = "Egg and Bacon",
        ingredients = setOf(
            Ingredient(
                material = Material(id = 1, name = "Egg"),
                measurement = Measurement(amount = 2.0, unit = SimpleUnit.PIECE),
            ),
            Ingredient(
                material = Material(id = 4, name = "Bacon"),
                measurement = Measurement(amount = 2.0, unit = SimpleUnit.PIECE),
            ),
        ),
    ),
    "Sunny Side Up" to Recipe(
        name = "Sunny Side Up",
        ingredients = setOf(
            Ingredient(
                material = Material(id = 1, name = "Egg"),
                measurement = Measurement(amount = 1.0, unit = SimpleUnit.PIECE),
            ),
        ),
    ),
    "Plain Bacon" to Recipe(
        name = "Plain Bacon",
        ingredients = setOf(
            Ingredient(
                material = Material(id = 4, name = "Bacon"),
                measurement = Measurement(amount = 1.0, unit = SimpleUnit.PIECE),
            ),
        ),
    ),
    "Creamy Omelette" to Recipe(
        name = "Creamy Omelette",
        ingredients = setOf(
            Ingredient(
                material = Material(id = 1, name = "Egg"),
                measurement = Measurement(amount = 2.0, unit = SimpleUnit.PIECE),
            ),
            Ingredient(
                material = Material(id = 2, name = "Milk"),
                measurement = Measurement(amount = 100.0, unit = SimpleUnit.MILLILITER),
            ),
        ),
    )
)

val recipeList = recipes
    .values
    .toList()
    .mapNotNull { either -> either.getOrNull() }