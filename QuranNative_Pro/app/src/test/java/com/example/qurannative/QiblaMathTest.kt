package com.example.qurannative

import com.example.qurannative.prayer.QiblaMath
import org.junit.Assert.assertEquals
import org.junit.Test

class QiblaMathTest {

    @Test
    fun normalizeDegreesHandlesNegativeValues() {
        assertEquals(
            350f,
            QiblaMath.normalizeDegrees(-10f),
            0.001f
        )
    }

    @Test
    fun normalizeDegreesHandlesValuesAbove360() {
        assertEquals(
            10f,
            QiblaMath.normalizeDegrees(370f),
            0.001f
        )
    }

    @Test
    fun shortestDifferenceCalculatesCorrectDirection() {
        assertEquals(
            20f,
            QiblaMath.shortestDifference(10f, 350f),
            0.001f
        )
    }

    @Test
    fun qiblaBearingFromMeccaIsApproximatelyNorth() {
        val bearing = QiblaMath.calculateBearing(
            latitude = 21.4225,
            longitude = 39.8262
        )

        assertEquals(0f, bearing.toFloat(), 0.1f)
    }
}
