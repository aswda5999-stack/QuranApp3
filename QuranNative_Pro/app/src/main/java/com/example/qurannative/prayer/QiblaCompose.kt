package com.example.qurannative.prayer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaCompose(
    qiblaBearing: Float,
    deviceAzimuth: Float
) {
    val direction = QiblaMath.normalizeDegrees(
        qiblaBearing - deviceAzimuth
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "اتجاه القبلة",
            fontSize = 26.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Box(
            modifier = Modifier.size(280.dp),
            contentAlignment = Alignment.Center
        ) {

            Canvas(
                modifier = Modifier.size(260.dp)
            ) {

                val center = Offset(
                    size.width / 2f,
                    size.height / 2f
                )

                val radius = size.minDimension * 0.43f

                drawCircle(
                    color = Color(0xFF789487),
                    radius = radius,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 5.dp.toPx()
                    )
                )

                rotate(
                    degrees = direction,
                    pivot = center
                ) {

                    drawLine(
                        color = Color(0xFFD9B66F),
                        start = center,
                        end = Offset(
                            center.x,
                            center.y - radius
                        ),
                        strokeWidth = 8.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    drawCircle(
                        color = Color(0xFFD9B66F),
                        radius = 12.dp.toPx(),
                        center = Offset(
                            center.x,
                            center.y - radius
                        )
                    )
                }
            }

            Text(
                text = "🕋",
                fontSize = 35.sp
            )
        }

        Text(
            text = "${direction.toInt()}°",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "حرّك هاتفك حتى يشير السهم نحو القبلة",
            color = MaterialTheme.colorScheme.secondary
        )
    }
}
