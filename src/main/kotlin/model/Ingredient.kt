package model

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.optics.optics

@optics
data class Ingredient (
    val material: Material,
    val measurement: Measurement,
) {
    companion object {
        operator fun invoke(
            material: Material,
            amount: Double,
            unit: SimpleUnit,
        ): Either<NegativeMeasurement, Ingredient> = either {
            ensure(amount >= 0.0) { NegativeMeasurement(material) }
            Ingredient(
                material = material,
                measurement = Measurement(amount = amount, unit = unit),
            )
        }

        operator fun invoke(
            material: Material,
            measurement: Measurement,
        ): Either<NegativeMeasurement, Ingredient> = either {
            ensure(measurement.amount >= 0.0) { NegativeMeasurement(material) }
            Ingredient(
                material = material,
                measurement = measurement,
            )
        }
    }

    override fun toString(): String = "$measurement of ${material.name}"
}

class NegativeMeasurement(private val material: Material) {
    override fun toString(): String = "${material.name} was initialized with less than 0 amount"
}