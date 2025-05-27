package com.rakib.composeui.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val content: String,
    val timestamp: Long,
    val isSent: Boolean
)