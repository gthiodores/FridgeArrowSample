package domain

import arrow.core.Either
import arrow.core.Nel
import arrow.core.raise.either
import kotlinx.coroutines.delay
import model.Fridge
import model.Recipe

class CookFromFridge(private val takeIngredientsFromFridge: TakeIngredientsFromFridge) {
    suspend operator fun invoke(
        fridge: Fridge,
        recipe: Recipe,
    ): Either<Nel<TakeIngredientsFromFridgeError>, Fridge> = either {
        println("Checking fridge for ingredients for ${recipe.name}")

        takeIngredientsFromFridge(recipe.ingredients, fridge)
            .onRight { _ ->
                println("Cooking ${recipe.name}")
                delay(5_000)
                println("Done cooking ${recipe.name}, enjoy your meal!")
            }
            .bind()
    }
}