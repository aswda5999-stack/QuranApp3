package com.example.qurannative.azkar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TasbihScreen(
    viewModel: TasbihViewModel,
    modifier: Modifier = Modifier
) {
    val counter by viewModel.counter.collectAsState()

    val progress = (
        counter.count.toFloat() /
            counter.target.coerceAtLeast(1L).toFloat()
        ).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "السبحة الإلكترونية",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "اذكر الله واطمئن قلبك",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "عدد التسبيحات",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = counter.count.toString(),
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Text(
                    text = "الهدف: ${counter.target}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(12.dp))

                androidx.compose.material3.LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "${(progress * 100).toInt()}%",
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(26.dp))

        Button(
            onClick = viewModel::increment,
            modifier = Modifier.size(190.dp),
            shape = CircleShape
        ) {
            Text(
                text = "تسبيح",
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = viewModel::reset,
                modifier = Modifier.weight(1f)
            ) {
                Text("تصفير")
            }

            OutlinedButton(
                onClick = { viewModel.setTarget(33L) },
                modifier = Modifier.weight(1f)
            ) {
                Text("هدف 33")
            }

            OutlinedButton(
                onClick = { viewModel.setTarget(100L) },
                modifier = Modifier.weight(1f)
            ) {
                Text("هدف 100")
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "اختر هدفاً مخصصاً",
            style = MaterialTheme.typography.titleSmall
        )

        Slider(
            value = counter.target.toFloat().coerceIn(1f, 1000f),
            onValueChange = {
                viewModel.setTarget(it.toLong().coerceAtLeast(1L))
            },
            valueRange = 1f..1000f
        )

        Text(
            text = "يمكنك تغيير الهدف في أي وقت",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
