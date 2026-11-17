package com.example.qurannative

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class QuranApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeApp()
    }

    private fun initializeApp() {
        // تهيئة الخدمات الأساسية للتطبيق
        // سيتم ربط قاعدة البيانات والإعدادات هنا
    }
}
