import arrow.core.flatMap
import domain.CookFromFridge
import domain.TakeIngredientsFromFridge
import kotlinx.coroutines.runBlocking
import model.defaultFridge
import model.recipeList

fun main() = runBlocking {
    val activity = CookFromFridge(takeIngredientsFromFridge = TakeIngredientsFromFridge())
    activity(fridge = defaultFridge, recipe = recipeList.first())
        .onRight { fridge -> println("\nFridge: ${fridge.contents} \n") }
        .flatMap { updatedFridge -> activity(updatedFridge, recipeList.first()) }
        .fold(
            { errors ->
                println("Failed to cook")
                println(errors.joinToString("\n"))
            },
            { fridge -> println("Fridge: ${fridge.contents}") }
        )
}