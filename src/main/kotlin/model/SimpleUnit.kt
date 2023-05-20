package model

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

enum class SimpleUnit(val unit: String, private val baseUnit: SimpleUnit? = null, private val modifier: Double) {
    GRAM("g", null, 1.0),
    KILOGRAM("kg", GRAM, 1000.0),
    MILLILITER("ml", null, 1.0),
    LITER("l", MILLILITER, 1000.0),
    PIECE("pcs", null, 1.0),
    TEASPOON("tsp", MILLILITER, 5.0);

    fun convertToBase(amount: Double): Either<FailedToConvertUnit, Double> = either {
        ensure(baseUnit != null) { FailedToConvertUnit(this@SimpleUnit) }
        amount * modifier
    }

    fun convertToAdvance(amount: Double): Either<FailedToConvertUnit, Double> = either {
        val advance = SimpleUnit.values().firstOrNull { item -> item.baseUnit == this@SimpleUnit }
        ensure(advance != null) { FailedToConvertUnit(this@SimpleUnit) }
        amount / advance.modifier
    }

    fun convert(amount: Double, to: SimpleUnit): Either<FailedToConvertUnit, Double> = either {
        when {
            to == this@SimpleUnit -> amount
            to == this@SimpleUnit.baseUnit -> convertToBase(amount).bind()
            to.baseUnit == this@SimpleUnit -> convertToAdvance(amount).bind()
            else -> raise(FailedToConvertUnit(this@SimpleUnit, to))
        }
    }
}

data class FailedToConvertUnit(val from: SimpleUnit, val to: SimpleUnit? = null) {
    override fun toString(): String {
        return when (to) {
            null -> "${from.unit} has no conversion"
            else -> "Cannot convert ${from.unit} to ${to.unit}"
        }
    }
}