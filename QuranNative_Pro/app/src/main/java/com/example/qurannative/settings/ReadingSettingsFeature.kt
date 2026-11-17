package com.example.qurannative.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.qurannative.design.QuranTheme

@Composable
fun ReadingSettingsScreen(
    preferences: AppPreferences,
    darkMode: Boolean,
    onDarkModeChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var fontSize by remember(preferences) {
        mutableFloatStateOf(preferences.quranFontSize)
    }

    var notifications by remember(preferences) {
        mutableStateOf(preferences.notificationsEnabled)
    }

    var vibration by remember(preferences) {
        mutableStateOf(preferences.vibrationEnabled)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "إعدادات التطبيق",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "خصص تجربة القراءة حسب رغبتك",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SettingSwitch(
                    title = "الوضع الليلي",
                    description = "تغيير مظهر التطبيق",
                    checked = darkMode,
                    onCheckedChange = onDarkModeChanged
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "حجم خط القرآن",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${fontSize.toInt()} نقطة",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Slider(
                    value = fontSize,
                    onValueChange = {
                        fontSize = it
                    },
                    valueRange = 16f..48f
                )

                Text(
                    text = "معاينة الخط",
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    fontSize = androidx.compose.ui.unit.TextUnit(
                        fontSize,
                        androidx.compose.ui.unit.TextUnitType.Sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                )

                Button(
                    onClick = {
                        preferences.quranFontSize = fontSize
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("حفظ حجم الخط")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingSwitch(
                    title = "التنبيهات",
                    description = "السماح بتنبيهات التطبيق",
                    checked = notifications,
                    onCheckedChange = {
                        notifications = it
                        preferences.notificationsEnabled = it
                    }
                )

                SettingSwitch(
                    title = "الاهتزاز",
                    description = "الاهتزاز عند التفاعل",
                    checked = vibration,
                    onCheckedChange = {
                        vibration = it
                        preferences.vibrationEnabled = it
                    }
                )
            }
        }

        Text(
            text = "القرآن الكريم • إصدار التطبيق",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
