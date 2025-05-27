package com.rakib.composeui.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rakib.composeui.data.local.dao.ChatDao
import com.rakib.composeui.data.local.entity.CallEntity
import com.rakib.composeui.data.local.entity.MessageEntity
import com.rakib.composeui.data.local.entity.UserEntity


@Database(entities = [UserEntity::class, MessageEntity::class, CallEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}