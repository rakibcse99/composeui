package com.rakib.composeui.presentation.di

import android.content.Context
import androidx.room.Room

import com.rakib.composeui.data.local.AppDatabase
import com.rakib.composeui.data.local.dao.ChatDao
import com.rakib.composeui.data.repository.ChatRepositoryImpl
import com.rakib.composeui.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "whatsapp_clone_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }

    @Provides
    @Singleton
    fun provideChatRepository(chatDao: ChatDao): ChatRepository {
        return ChatRepositoryImpl(chatDao)
    }
}