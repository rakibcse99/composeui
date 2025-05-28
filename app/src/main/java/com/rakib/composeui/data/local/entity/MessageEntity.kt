package com.rakib.composeui.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Int,
    val senderId: String,
    val text: String,
    val timestamp: Long
)