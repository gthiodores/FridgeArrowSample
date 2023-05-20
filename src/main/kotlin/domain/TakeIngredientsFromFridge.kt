package domain

import arrow.core.Either
import arrow.core.Nel
import arrow.core.mapOrAccumulate
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import domain.TakeIngredientsFromFridgeError.*
import model.*

class TakeIngredientsFromFridge {
    /**
     * Take ingredients from fridge and return a fridge with updated contents
     */
    operator fun invoke(items: Set<Ingredient>, fridge: Fridge): Either<Nel<TakeIngredientsFromFridgeError>, Fridge> {
        return either {
            items
                .mapOrAccumulate { item -> ensure(item, fridge) }
                .map { items -> items.fold(fridge) { acc, item -> acc.take(item) } }
                .bind()
        }
    }

    private fun Fridge.take(item: Ingredient): Fridge {
        val inFridge = mappedAmount[item.material.id]!!
        val newAmount = inFridge.subtract(item.measurement).getOrNull()!!

        return mappedContents
            .toMutableMap()
            .apply { replace(item.material.id, item.copy(measurement = newAmount)) }
            .let { new -> copy(contents = new.values.toList().filterNot { content -> content.measurement.isEmpty }) }
    }

    private fun Raise<TakeIngredientsFromFridgeError>.ensure(
        ingredient: Ingredient,
        fridge: Fridge
    ): Ingredient {
        val inFridge = fridge.mappedAmount[ingredient.material.id]

        ensureNotNull(inFridge) { MaterialNotFound(ingredient.material) }

        inFridge
            .compare(ingredient.measurement)
            .onLeft { err -> raise(IncomparableUnit(err.from, err.to!!)) }
            .onRight { value ->
                ensure(value >= 0) {
                    NotEnoughMaterial(
                        ingredient.material,
                        fridge.mappedAmount[ingredient.material.id]!!,
                        ingredient.measurement
                    )
                }
            }

        return ingredient
    }
}

sealed interface TakeIngredientsFromFridgeError {
    class MaterialNotFound(private val material: Material) : TakeIngredientsFromFridgeError {
        override fun toString(): String = "${material.name} was not found in the fridge"
    }

    class NotEnoughMaterial(
        private val material: Material,
        private val inFridge: Measurement,
        private val required: Measurement,
    ) : TakeIngredientsFromFridgeError {
        override fun toString(): String = StringBuilder()
            .append("Required $required but ")
            .append("only $inFridge of ${material.name} ")
            .append("was found in the fridge")
            .toString()
    }

    class IncomparableUnit(private val from: SimpleUnit, private val to: SimpleUnit) : TakeIngredientsFromFridgeError {
        override fun toString(): String = "Cannot compare $from with $to"
    }
}