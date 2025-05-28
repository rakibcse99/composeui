package com.rakib.composeui

import android.app.Application
import androidx.room.Room
import com.rakib.composeui.data.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WhatsAppProApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "whatsapp_pro_db"
        ).build()
    }
}