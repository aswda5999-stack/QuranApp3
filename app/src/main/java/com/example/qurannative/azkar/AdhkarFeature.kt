package com.example.qurannative.azkar

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

enum class AdhkarCategory(val title: String) {
    MORNING("أذكار الصباح"),
    EVENING("أذكار المساء"),
    GENERAL("أذكار عامة")
}

data class DhikrItem(
    val id: Int,
    val text: String,
    val category: AdhkarCategory,
    val repeatCount: Int = 1
)

object AdhkarCatalog {
    val items = listOf(
        DhikrItem(1, "سبحان الله", AdhkarCategory.GENERAL, 33),
        DhikrItem(2, "الحمد لله", AdhkarCategory.GENERAL, 33),
        DhikrItem(3, "الله أكبر", AdhkarCategory.GENERAL, 34),
        DhikrItem(4, "لا إله إلا الله", AdhkarCategory.GENERAL, 1),
        DhikrItem(5, "أستغفر الله", AdhkarCategory.GENERAL, 100),
        DhikrItem(6, "سبحان الله وبحمده", AdhkarCategory.MORNING, 100),
        DhikrItem(7, "أستغفر الله وأتوب إليه", AdhkarCategory.EVENING, 100)
    )
}

@Composable
fun AdhkarScreen(
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember {
        mutableStateOf(AdhkarCategory.MORNING)
    }

    val counters = remember {
        mutableStateMapOf<Int, Int>()
    }

    val context = LocalContext.current

    val visibleItems = remember(selectedCategory) {
        AdhkarCatalog.items.filter {
            it.category == selectedCategory
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "الأذكار",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AdhkarCategory.entries.forEach { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category.title) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(visibleItems, key = { it.id }) { item ->
                val count = counters[item.id] ?: 0

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "العدد: $count / ${item.repeatCount}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    counters[item.id] =
                                        (count + 1).coerceAtMost(item.repeatCount)
                                },
                                enabled = count < item.repeatCount
                            ) {
                                Text("تسبيح")
                            }

                            OutlinedButton(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    counters[item.id] = 0
                                }
                            ) {
                                Text("تصفير")
                            }
                        }

                        if (count >= item.repeatCount) {
                            Text(
                                text = "أحسنت، اكتمل العدد",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                AdhkarReminderCard(context)
            }
        }
    }
}

@Composable
private fun AdhkarReminderCard(context: Context) {
    val preferences = remember(context) {
        context.getSharedPreferences(
            "adhkar_reminder_settings",
            Context.MODE_PRIVATE
        )
    }

    var enabled by remember {
        mutableStateOf(preferences.getBoolean("enabled", false))
    }

    var interval by remember {
        mutableIntStateOf(preferences.getInt("interval", 60))
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "التذكير بالأذكار",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("تفعيل التذكير")
                Switch(
                    checked = enabled,
                    onCheckedChange = { checked ->
                        enabled = checked
                        preferences.edit()
                            .putBoolean("enabled", checked)
                            .apply()

                        if (checked) {
                            AdhkarReminderScheduler.schedule(
                                context,
                                interval.toLong()
                            )
                        } else {
                            AdhkarReminderScheduler.cancel(context)
                        }
                    }
                )
            }

            Text("الفاصل بين التذكيرات: $interval دقيقة")

            Slider(
                value = interval.toFloat(),
                onValueChange = {
                    interval = it.toInt()
                },
                onValueChangeFinished = {
                    preferences.edit()
                        .putInt("interval", interval)
                        .apply()

                    if (enabled) {
                        AdhkarReminderScheduler.schedule(
                            context,
                            interval.toLong()
                        )
                    }
                },
                valueRange = 15f..240f,
                steps = 14
            )

            Text(
                text = "قد يؤخر أندرويد الإشعارات حسب وضع توفير البطارية.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

class AdhkarReminderWorker(
    context: Context,
    parameters: WorkerParameters
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result {
        return try {
            val context = applicationContext
            val channelId = "adhkar_reminders"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = context.getSystemService(
                    Context.NOTIFICATION_SERVICE
                ) as android.app.NotificationManager

                val channel = android.app.NotificationChannel(
                    channelId,
                    "تذكيرات الأذكار",
                    android.app.NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "تذكير المستخدم بقراءة الأذكار"
                }

                manager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(
                context,
                channelId
            )
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("القرآن الكريم")
                .setContentText("لا تنسَ ذكر الله، سبحان الله وبحمده")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build()

            val permissionGranted =
                Build.VERSION.SDK_INT < 33 ||
                context.checkSelfPermission(
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

            if (permissionGranted) {
                try {
                    NotificationManagerCompat.from(context)
                        .notify(4101, notification)
                } catch (_: SecurityException) {
                    // قد تكون صلاحية الإشعارات غير متاحة.
                }
            }

            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}

object AdhkarReminderScheduler {

    private const val WORK_NAME = "quran_adhkar_reminder"

    fun schedule(
        context: Context,
        intervalMinutes: Long = 60
    ) {
        val safeInterval = intervalMinutes.coerceAtLeast(15)

        val request =
            PeriodicWorkRequestBuilder<AdhkarReminderWorker>(
                safeInterval,
                TimeUnit.MINUTES
            ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context)
            .cancelUniqueWork(WORK_NAME)
    }
}
