package com.example.qurannative.prayer

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import kotlin.math.PI

object QiblaMath {

    private const val KAABA_LAT = 21.4225
    private const val KAABA_LON = 39.8262

    fun calculateBearing(
        latitude: Double,
        longitude: Double
    ): Double {

        val lat1 = Math.toRadians(latitude)
        val lat2 = Math.toRadians(KAABA_LAT)

        val deltaLon = Math.toRadians(
            KAABA_LON - longitude
        )

        val y = sin(deltaLon)

        val x = cos(lat1) * tan(lat2) -
                sin(lat1) * cos(deltaLon)

        val bearing = Math.toDegrees(
            atan2(y, x)
        )

        return (bearing + 360.0) % 360.0
    }

    fun normalizeDegrees(degrees: Float): Float {
        return ((degrees % 360f) + 360f) % 360f
    }

    fun shortestDifference(
        target: Float,
        current: Float
    ): Float {
        return ((target - current + 540f) % 360f) - 180f
    }
}
