package model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class SimpleUnitTest {
    @Test
    fun `convert upward should succeed`() {
        SimpleUnit
            .GRAM
            .convert(1.0, SimpleUnit.KILOGRAM)
            .fold(
                { e -> fail("$e (Should never fail)") },
                { amount -> assertEquals(0.001, amount, "1 / 1000 = 0.1") }
            )
    }

    @Test
    fun `convert downward should succeed`() {
        SimpleUnit
            .KILOGRAM
            .convert(1.0, SimpleUnit.GRAM)
            .fold(
                { e -> fail("$e (Should never fail)") },
                { amount -> assertEquals(1000.0, amount, "1 * 1000 = 1000") }
            )
    }

    @Test
    fun `invalid conversion should fail`() {
        SimpleUnit
            .GRAM
            .convert(1.0, SimpleUnit.PIECE)
            .fold(
                { e -> assertEquals("Cannot convert g to pcs", e.toString()) },
                { _ -> fail("Should fail") }
            )
    }

    @Test
    fun `base conversion without link should fail`() {
        SimpleUnit
            .PIECE
            .convertToBase(1.0)
            .fold(
                { e -> assertEquals("pcs has no conversion", e.toString()) },
                { _ -> fail("Should fail") }
            )
    }

    @Test
    fun `advance conversion without link should fail`() {
        SimpleUnit
            .PIECE
            .convertToAdvance(1.0)
            .fold(
                { e -> assertEquals("pcs has no conversion", e.toString()) },
                { _ -> fail("Should fail") }
            )
    }
}