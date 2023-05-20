package model

import arrow.core.Either
import arrow.core.raise.either
import arrow.optics.optics

@optics
data class Measurement(
    val amount: Double,
    val unit: SimpleUnit,
) {
    val isEmpty: Boolean
        get() = amount == 0.0

    fun convert(target: SimpleUnit): Either<FailedToConvertUnit, Measurement> = either {
        unit
            .convert(amount, target)
            .map { amount -> copy(amount = amount, unit = target) }
            .bind()
    }

    fun compare(other: Measurement): Either<FailedToConvertUnit, Int> = unit
        .convert(amount, other.unit)
        .map { amount -> amount.compareTo(other.amount) }

    fun add(other: Measurement): Either<FailedToConvertUnit, Measurement> = convert(other.unit)
        .map { measure -> measure.copy(amount = other.amount + measure.amount) }

    fun subtract(other: Measurement): Either<FailedToConvertUnit, Measurement> = convert(other.unit)
        .map { measure -> measure.copy(amount = measure.amount - other.amount) }

    override fun toString(): String = "$amount ${unit.unit}"

    companion object
}
