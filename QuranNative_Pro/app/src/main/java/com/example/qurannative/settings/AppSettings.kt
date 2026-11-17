package com.example.qurannative.settings

import android.content.Context
import androidx.core.content.edit

class AppSettings(context: Context) {

    private val preferences = context.getSharedPreferences(
        "quran_app_settings",
        Context.MODE_PRIVATE
    )

    var darkMode: Boolean
        get() = preferences.getBoolean(KEY_DARK_MODE, false)
        set(value) {
            preferences.edit {
                putBoolean(KEY_DARK_MODE, value)
            }
        }

    var quranFontSize: Float
        get() = preferences.getFloat(KEY_FONT_SIZE, 26f)
        set(value) {
            preferences.edit {
                putFloat(
                    KEY_FONT_SIZE,
                    value.coerceIn(16f, 48f)
                )
            }
        }

    var notificationsEnabled: Boolean
        get() = preferences.getBoolean(KEY_NOTIFICATIONS, true)
        set(value) {
            preferences.edit {
                putBoolean(KEY_NOTIFICATIONS, value)
            }
        }

    var vibrationEnabled: Boolean
        get() = preferences.getBoolean(KEY_VIBRATION, true)
        set(value) {
            preferences.edit {
                putBoolean(KEY_VIBRATION, value)
            }
        }

    fun reset() {
        preferences.edit {
            clear()
        }
    }

    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_FONT_SIZE = "quran_font_size"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_VIBRATION = "vibration_enabled"
    }
}
